package dragon.model.food;

/**
 * Created by lin.cheng
 */
public class Record {
    Long id;
    Long resid;
    Long goTime;
    Boolean veto;
    Long gId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResid() {
        return resid;
    }

    public void setResid(Long resid) {
        this.resid = resid;
    }

    public Long getGoTime() {
        return goTime;
    }

    public void setGoTime(Long goTime) {
        this.goTime = goTime;
    }

    public Boolean getVeto() {
        return veto;
    }

    public void setVeto(Boolean veto) {
        this.veto = veto;
    }

    public Long getgId() {
        return gId;
    }

    public void setgId(Long gId) {
        this.gId = gId;
    }
}
