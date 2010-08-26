package org.flexpay.eirc.persistence.sorter;

public class EircAccountSorterByAccountNumber extends EircAccountSorter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOrderBy(StringBuilder orderByClause) {
        orderByClause.append(" a.accountNumber ").append(getOrder());
    }

}
