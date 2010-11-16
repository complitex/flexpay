package org.flexpay.common.persistence.registry.sorter;

public class RecordErrorsGroupSorterByName extends RecordErrorsGroupSorter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOrderBy(StringBuilder orderByClause) {
        // Not implemented
    }

    @Override
    public void setOrderBySQL(StringBuilder orderByClause) {
        orderByClause.append(" town_name, street_type, street_name, building_number, bulk_number, apartment_number, last_name, first_name, middle_name ").append(getOrder());
    }
}
