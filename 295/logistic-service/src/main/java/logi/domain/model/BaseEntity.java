package logi.domain.model;

import javax.persistence.*;

@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(generator = "bcSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "bcSeq", sequenceName = "bc_id_seq")
    Long id;

    @Column(name = "last_modified_time")
    private long lastModified;
    @Column(name = "creation_time")
    private long creationTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    @PreUpdate
    @PrePersist
    protected void beforeUpdate() {
        long time = System.currentTimeMillis();
        if (creationTime == 0) creationTime = time;
        lastModified = time;
    }
}
