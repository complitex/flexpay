package org.flexpay.common.persistence.filter;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.math.BigDecimal;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

public class BigDecimalValueFilter extends StringValueFilter {

    private BigDecimal bdValue = null;

    public BigDecimalValueFilter() {
    }

    public BigDecimalValueFilter(BigDecimal bdValue) {
        setBdValue(bdValue);
    }

    public BigDecimalValueFilter(String value) {
        setValue(value);
    }

    public BigDecimal getBdValue() {
        return bdValue;
    }

    public void setBdValue(BigDecimal bdValue) {
        this.bdValue = bdValue;
        if (bdValue != null) {
            super.setValue(bdValue.toString());
        }
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        if (isNotEmpty(value)) {
            try {
                bdValue = new BigDecimal(value);
            } catch (NumberFormatException e) {
                bdValue = null;
            }
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("bdValue", bdValue).
                append("value", value).
                toString();
    }
}
