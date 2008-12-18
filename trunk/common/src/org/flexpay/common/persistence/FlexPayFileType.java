package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class FlexPayFileType extends DomainObject {

    private String name;
    private String description;
    private String fileMask;
    private FlexPayModule module;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileMask() {
        return fileMask;
    }

    public void setFileMask(String fileMask) {
        this.fileMask = fileMask;
    }

    public FlexPayModule getModule() {
        return module;
    }

    public void setModule(FlexPayModule module) {
        this.module = module;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("FlexPayFileType {").
                append("id", getId()).
                append("name", name).
                append("description", description).
                append("fileMask", fileMask).
                append("module", module).
                append("}").toString();
    }

}
