package org.flexpay.payments.persistence.filters;

import org.flexpay.common.persistence.filter.BigDecimalValueFilter;

import java.math.BigDecimal;

public class MinimalSumFilter extends BigDecimalValueFilter {

    public MinimalSumFilter() {
    }

    public MinimalSumFilter(BigDecimal bdValue) {
        super(bdValue);
    }

    public MinimalSumFilter(String value) {
        super(value);
    }

}
