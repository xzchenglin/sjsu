package app.domain.model;

import javax.persistence.*;

@MappedSuperclass
public class VersionedEntity extends BaseEntity {

    @Version
    @Column(name = "entity_version")
    private Long entityVersion;

    public Long getEntityVersion() {
        return entityVersion;
    }

    public void setEntityVersion(Long version) {
        this.entityVersion = version;
    }

}
