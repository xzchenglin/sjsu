package helper;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import common.JsonHelper;
import model.Post;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DynamoHelper {

    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_WEST_1)
            .build();
    static DynamoDB dynamoDB = new DynamoDB(client);
    static Table table = dynamoDB.getTable("post");

    public static void write(Post post){
        Item item = new Item()
                .withPrimaryKey("gid", post.getGid())
                .withLong("uid", post.getUid())
                .withLong("time", post.getTime())
                .withString("msg", post.getMsg());

        PutItemOutcome outcome = table.putItem(item);
        outcome.getPutItemResult().toString();
    }

    public static List<Post> search(Long gid) throws Exception{

        ItemCollection<QueryOutcome> items = table.query("gid", gid);

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
