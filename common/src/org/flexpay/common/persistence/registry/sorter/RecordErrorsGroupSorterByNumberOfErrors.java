package org.flexpay.common.persistence.registry.sorter;

public class RecordErrorsGroupSorterByNumberOfErrors extends RecordErrorsGroupSorter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOrderBy(StringBuilder orderByClause) {
        // Not implemented
    }

    @Override
    public void setOrderBySQL(StringBuilder orderByClause) {
        orderByClause.append(" count ").append(getOrder());
    }
}
