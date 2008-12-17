package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class FlexPayFileStatus extends DomainObject {

    private String title;
    private String description;
    private FlexPayModule module;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                append("FlexPayFileStatus {").
                append("id", getId()).
                append("title", title).
                append("description", description).
                append("module", module).
                append("}").toString();
    }

}
