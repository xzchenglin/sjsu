package bc;

import org.apache.commons.lang.StringUtils;

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

    public void sanityCheck() throws Exception {
        if(StringUtils.isBlank(pid) || StringUtils.isBlank(pukNext) || StringUtils.isBlank(payload)){
            throw new Exception("Invalid block.");
        }
    }
}
