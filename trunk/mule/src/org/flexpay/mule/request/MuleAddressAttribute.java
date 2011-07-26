package org.flexpay.mule.request;

import org.apache.commons.lang.builder.ToStringBuilder;

public class MuleAddressAttribute {

    private Long id;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("value", value).
                toString();
    }
}
