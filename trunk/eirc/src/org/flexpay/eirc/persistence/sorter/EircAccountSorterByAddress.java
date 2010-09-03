package org.flexpay.eirc.persistence.sorter;

public class EircAccountSorterByAddress extends EircAccountSorter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOrderBy(StringBuilder orderByClause) {
        orderByClause.append(" upper(ifnull(ci.townType,'')), ").
                append("upper(ifnull(ci.townName,'')), ").
                append("upper(ifnull(ci.streetTypeName,'')), ").
                append("upper(ifnull(ci.streetName,'')), ").
                append("lpad(convert(ifnull(ci.buildingNumber, '0'), UNSIGNED), 10, '0'), ").
                append("lpad(convert(ifnull(ci.buildingBulk, '0'), UNSIGNED), 10, '0'), ").
                append("lpad(convert(ifnull(ci.apartmentNumber, '0'), UNSIGNED), 10, '0') ").
                append(getOrder());
    }

}
