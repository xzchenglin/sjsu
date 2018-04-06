package dragon.model.food;

/**
 * Created by lin.cheng
 */
public class Vote {
    String email;
    Long recId;
    Result result;
    String ip;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getRecId() {
        return recId;
    }

    public void setRecId(Long recId) {
        this.recId = recId;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "email='" + email + '\'' +
                ", recId=" + recId +
                ", result=" + result +
                '}';
    }

    public static enum Result {
        killme(-1),
        dislike(-1),
        like(1);

        int score;

        public int getScore() {
            return score;
        }

        Result(int score) {
            this.score = score;
        }
    }
}
