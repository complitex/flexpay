package org.flexpay.common.persistence.filter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.filter.ObjectFilter;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

public class StringValueFilter extends ObjectFilter {

    protected String value = "";

    public StringValueFilter() {
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
        return new ToStringBuilder(this).
                append("value", value).
                toString();
    }

}
