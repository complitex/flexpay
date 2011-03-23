package org.flexpay.common.persistence.filter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

public class StringValueFilter extends ObjectFilter {

    public final static Long TYPE_TOWN_TYPE = 0L;
    public final static Long TYPE_TOWN = 1L;
    public final static Long TYPE_STREET_TYPE = 6L;
    public final static Long TYPE_STREET = 2L;
    public final static Long TYPE_BUILDING = 3L;
    public final static Long TYPE_APARTMENT = 4L;
    public final static Long TYPE_FIO = 5L;

    protected String value = "";
    private Long type;

    public StringValueFilter() {
    }

    public StringValueFilter(String value, Long type) {
        this.value = value;
        this.type = type;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @Override
    public boolean needFilter() {
        return isNotEmpty(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("value", value).
                append("type", type).
                toString();
    }

}
