package model;

public class Post {
    Long uid;
    Long gid;
    Long time;
    String msg;

    public Post() {
    }

    public Post(Long uid, Long gid, Long time, String msg) {
        this.uid = uid;
        this.gid = gid;
        this.time = time;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Post{" +
                "uid=" + uid +
                ", gid=" + gid +
                ", time=" + time +
                ", msg='" + msg + '\'' +
                '}';
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
