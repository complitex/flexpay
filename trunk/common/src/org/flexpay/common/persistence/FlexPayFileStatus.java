package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class FlexPayFileStatus extends DomainObject {

    private String name;
    private String description;
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
                append("name", name).
                append("description", description).
                append("module", module).
                append("}").toString();
    }

}
