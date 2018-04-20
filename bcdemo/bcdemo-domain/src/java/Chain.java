import helper.JSONHelper;
import helper.Utils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Chain {

    List<Block> blocks = new ArrayList<Block>();

    public List<Block> getBlocks() {
        return blocks;
    }

    public String append(Block b) throws Exception{
        boolean ex = false;
        for (Block block:blocks){
            if(b.pid.equals(block.pid)){
                ex = true;
                break;
            }
        }

        String currentPuk = null;
        if(ex){
            Block prev = blocks.get(blocks.size()-1);
            currentPuk = prev.pukNext;
            String payload = Utils.rsaDec(b.payload, currentPuk);
            if(!StringUtils.equals(payload, Utils.md5(JSONHelper.toJson(prev)))){
                System.out.println("payload not match!");
                return null;
            }
            System.out.println(b.pid + ": " + currentPuk + "->" + b.pukNext);
        } else {
            currentPuk = b.pukNext;
            String payload = Utils.rsaDec(b.payload,currentPuk);
            if(!StringUtils.equals(payload, b.pid)){
                System.out.println("Pid not match!");
                return null;
            }
            System.out.println(b.pid + ": " + currentPuk);
        }

        blocks.add(b);

        return Utils.md5(JSONHelper.toJson(b));
    }

}
