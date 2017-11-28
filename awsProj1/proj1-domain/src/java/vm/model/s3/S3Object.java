package domain.model.s3;

import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Lin Cheng on 2017/10/8.
 */
public class S3Object implements Comparable {
    private String name;
    private String url;
    private String fullPath;
    private String localPath;
    private String desc;
    private String userFirstName;
    private String userLastName;
    private long uploadTime;
    private long updateTime;
    private String meta;
    private boolean isFolder;
    private S3Object parent;
    private Set<S3Object> children = new TreeSet<>();

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public S3Object getParent() {
        return parent;
    }

    public void setParent(S3Object parent) {
        this.parent = parent;
    }

    public Set<S3Object> getChildren() {
        return children;
    }

    public void setChildren(Set<S3Object> children) {
        this.children = children;
    }

    public void addChild(S3Object c) {
        children.add(c);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return StringUtils.equals(this.getName(), ((S3Object)obj).getName());
    }

    @Override
    public int compareTo(Object o) {
        return name.compareTo(((S3Object)o).getName());
    }
}
