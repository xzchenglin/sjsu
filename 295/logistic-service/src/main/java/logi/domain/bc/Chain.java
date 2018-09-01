package logi.domain.bc;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.stereotype.Component;
import logi.comm.JSONHelper;
import logi.comm.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lin Cheng
 */
@Component
public class Chain {

    List<Block> blocks = new ArrayList<Block>();

    public List<Block> getBlocks() {
        return blocks;
    }

    public Chain append(Block b) throws Exception{

        b.sanityCheck();

        boolean ex = blocks != null && blocks.size() > 0;

        String currentPuk = null;
        if(ex){
            Block prev = blocks.get(blocks.size()-1);
            currentPuk = prev.pukNext;
            String payload = Utils.rsaDec(b.payload, currentPuk);
            if(!StringUtils.equals(payload, Utils.md5(JSONHelper.toJson(prev)))){
                throw new Exception("payload not match!");
            }
            System.out.println(b.pid + ": " + currentPuk + "->" + b.pukNext);
        } else {
            currentPuk = b.pukNext;
            String payload = Utils.rsaDec(b.payload,currentPuk);
            if(!StringUtils.equals(payload, b.pid)){
                throw new Exception("Pid not match!");
            }
            System.out.println(b.pid + ": " + currentPuk);
        }

        blocks.add(b);

        return this;
    }

    @JsonIgnore
    public Block getLast(){
        if(blocks.size() == 0){
            return null;
        }
        return blocks.get(blocks.size()-1);
    }

    public boolean verify(){
        if(blocks == null || blocks.size()<2){
            return true;
        }
        for(int i=1; i<blocks.size(); i++){
            Block current = blocks.get(i);
            Block prev = blocks.get(i-1);

            try{
                String payload = Utils.rsaDec(current.payload, prev.pukNext);
                if(!StringUtils.equals(payload, Utils.md5(JSONHelper.toJson(prev)))){
                    return false;
                }
            } catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

}
