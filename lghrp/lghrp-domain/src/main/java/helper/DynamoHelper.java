package helper;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import common.JsonHelper;
import model.Post;

import java.util.*;

public class DynamoHelper {

    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_WEST_1)
            .build();
    static DynamoDB dynamoDB = new DynamoDB(client);
    static Table table = dynamoDB.getTable("post");

    public static void write(Post post){

        List<Map<String, Object>> list = new ArrayList<>();

        if(post.getComments() != null) {
            for (Post.Comment c : post.getComments()) {
                Map<String, Object> map = new HashMap<>();
                map.put("uid", c.getUid());
                map.put("time", c.getTime());
                map.put("msg", c.getMsg());
                list.add(map);
            }
        }

        Item item = new Item()
                .withPrimaryKey("gid", post.getGid())
                .withLong("uid", post.getUid())
                .withLong("time", post.getTime())
                .withList("comments", list)
                .withString("msg", post.getMsg());

        //Automatically do update if pkey+sKey same
        PutItemOutcome outcome = table.putItem(item);
        outcome.getPutItemResult().toString();
    }

    public static List<Post> search(Long gid) throws Exception{

        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("gid = :v_id")
                .withScanIndexForward(false)
                .withValueMap(new ValueMap()
                        .withLong(":v_id", gid));
        ItemCollection<QueryOutcome> items = table.query(spec);

        List<Post> ret = new ArrayList<>();
        Iterator<Item> iterator = items.iterator();
        Item item = null;
        while (iterator.hasNext()) {
            item = iterator.next();
            ret.add(JsonHelper.fromJson2(item.toJSON(), Post.class));
        }
        return ret;
    }
}
