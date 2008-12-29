package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.util.FPFileUtil;

import java.io.File;
import java.util.Date;

public class FPFile extends DomainObject {

    private String nameOnServer;
    private String originalName;
    private Long size;
    private String userName;
    private String description;
    private Date creationDate = new Date();
    private FPModule module;

    public String getNameOnServer() {
        return nameOnServer;
    }

    public void setNameOnServer(String nameOnServer) {
        this.nameOnServer = nameOnServer;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public File getFile() {
        return new File(FPFileUtil.getFileLocalPath(this));
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public FPModule getModule() {
        return module;
    }

    public void setModule(FPModule module) {
        this.module = module;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("FPFile {").
                append("id", getId()).
                append("nameOnServer", nameOnServer).
                append("originalName", originalName).
                append("size", size).
                append("userName", userName).
                append("creationDate", creationDate).
                append("}").toString();
    }

}
