package com.ces.config.scheme;

import com.ces.xarch.core.entity.StringIDEntity;

public class DefaultDocumentScheme extends StringIDEntity {

    private static final long serialVersionUID = 5760276608168074160L;
    private String tableId;
    private String originName;
    private String realName;
    private String fileSize;
    private String fileFormat;
    private String location;
    private String path;
    private String createUser;
    private String createTime;
    private String ownerId;
    private String ownerTableId;
    
    public String getTableId() {
        return tableId;
    }
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
    public String getOriginName() {
        return originName;
    }
    public void setOriginName(String originName) {
        this.originName = originName;
    }
    public String getRealName() {
        return realName;
    }
    public void setRealName(String realName) {
        this.realName = realName;
    }
    public String getFileSize() {
        return fileSize;
    }
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
    public String getFileFormat() {
        return fileFormat;
    }
    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getCreateUser() {
        return createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    public String getOwnerTableId() {
        return ownerTableId;
    }
    public void setOwnerTableId(String ownerTableId) {
        this.ownerTableId = ownerTableId;
    }
    
    public String getFilePath() {
        String filePath = getLocation().replace("\\", "/");
        if (getLocation().endsWith("/") || getPath().startsWith("/")) {
            filePath = getLocation() + getPath();
        } else {
            filePath = getLocation() + "/" + getPath();
        }
        if (filePath.endsWith("/")) {
            filePath += getRealName() + "." + getFileFormat();
        } else {
            filePath += "/" + getRealName() + "." + getFileFormat();
        }
        return filePath;
    }
    
    public String getFileName() {
        return getOriginName() + "." + getFileFormat();
    }
}
