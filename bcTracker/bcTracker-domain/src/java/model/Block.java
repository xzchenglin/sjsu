package model;

/**
 * Created by Lin Cheng
 */
public class Block {
    public String pid;
    public String pukNext;
    public String payload;

    public Block() {
    }

    public Block(String pid, String pukNext, String payload) {
        this.pid = pid;
        this.pukNext = pukNext;
        this.payload = payload;
    }
}
