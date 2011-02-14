package org.flexpay.payments.persistence.filters;

import org.flexpay.common.persistence.filter.BigDecimalValueFilter;

import java.math.BigDecimal;

public class MaximalSumFilter extends BigDecimalValueFilter {

    public MaximalSumFilter() {
    }

    public MaximalSumFilter(BigDecimal bdValue) {
        super(bdValue);
    }

    public MaximalSumFilter(String value) {
        super(value);
    }
}
