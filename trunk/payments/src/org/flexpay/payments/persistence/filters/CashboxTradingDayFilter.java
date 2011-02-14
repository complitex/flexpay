package org.flexpay.payments.persistence.filters;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;

public class CashboxTradingDayFilter extends PrimaryKeyFilter<DomainObject> {

    public CashboxTradingDayFilter() {
        super(-1L);
    }

    public CashboxTradingDayFilter(Long selectedId) {
        super(selectedId);
    }

}
