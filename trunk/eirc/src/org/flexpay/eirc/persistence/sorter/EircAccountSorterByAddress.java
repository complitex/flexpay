package org.flexpay.eirc.persistence.sorter;

public class EircAccountSorterByAddress extends EircAccountSorter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOrderBy(StringBuilder orderByClause) {
        orderByClause.append(" r.creationDate ").append(getOrder());
    }

}
