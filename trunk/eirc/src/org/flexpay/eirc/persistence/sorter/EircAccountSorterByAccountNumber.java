package org.flexpay.eirc.persistence.sorter;

public class EircAccountSorterByAccountNumber extends EircAccountSorter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOrderBy(StringBuilder orderByClause) {
        orderByClause.append(" a.accountNumber ").append(getOrder());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOrderBySQL(StringBuilder orderByClause) {
        orderByClause.append(" sum.account_number ").append(getOrder());
    }

}
