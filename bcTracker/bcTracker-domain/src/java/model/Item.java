package model;

import bc.Chain;
import helper.JSONHelper;
import helper.Utils;

/**
 * Created by Lin Cheng
 */
public class Item {
    String id;
    String name;
    String nextpubkey;
    String chain;

    //transit
    String payload;
    String hash;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNextpubkey() {
        return nextpubkey;
    }

    public void setNextpubkey(String nextpubkey) {
        this.nextpubkey = nextpubkey;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Item applyHash(){
        try {
            this.setHash(Utils.md5(JSONHelper.toJson(JSONHelper.fromJson2(chain, Chain.class).getLast())));
            this.setChain(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}
