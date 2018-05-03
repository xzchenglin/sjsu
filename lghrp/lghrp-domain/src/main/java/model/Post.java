package model;

import java.util.List;

public class Post {
    Long uid;
    Long gid;
    Long time;
    String msg;
    List<Comment> comments;

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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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

    public static class Comment {
        Long uid;
        Long time;
        String msg;

        public Long getUid() {
            return uid;
        }

        public Long getTime() {
            return time;
        }

        public String getMsg() {
            return msg;
        }

        public Comment() {
        }

        public Comment(Long uid, Long time, String msg) {
            this.uid = uid;
            this.time = time;
            this.msg = msg;
        }
    }
}
