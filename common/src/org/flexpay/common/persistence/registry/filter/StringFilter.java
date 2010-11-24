package org.flexpay.common.persistence.registry.filter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.filter.ObjectFilter;

public class StringFilter extends ObjectFilter {

    public final static Long TYPE_TOWN = 1L;
    public final static Long TYPE_STREET = 2L;
    public final static Long TYPE_BUILDING = 3L;
    public final static Long TYPE_APARTMENT = 4L;
    public final static Long TYPE_FIO = 5L;

    private String value = "";
    private Long type;

    public StringFilter() {
    }

    public StringFilter(String value, Long type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("value", value).
                append("type", type).
                toString();
    }
}
