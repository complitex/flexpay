package org.flexpay.payments.actions.registry.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.*;
import org.flexpay.common.persistence.registry.RecordErrorsGroup;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.flexpay.common.util.CollectionUtils.list;

public class RecordErrorsGroupView {

    private String name = "";
    private RecordErrorsGroup group;

    public RecordErrorsGroupView() {

    }

    public RecordErrorsGroupView(@NotNull RecordErrorsGroup group, ClassToTypeRegistry typeRegistry) {
        this.group = group;
        setName(typeRegistry);
    }

    public String getTownName() {
        return group.getTownName() != null ? group.getTownName().replace("\"", "\\\"") : null;
    }

    public String getStreetType() {
        return group.getStreetType() != null ? group.getStreetType().replace("\"", "\\\"") : null;
    }

    public String getStreetName() {
        return group.getStreetName() != null ? group.getStreetName().replace("\"", "\\\"") : null;
    }

    public String getBuildingNumber() {
        return group.getBuildingNumber() != null ? group.getBuildingNumber().replace("\"", "\\\"") : null;
    }

    public String getBuildingBulk() {
        return group.getBuildingBulk() != null ? group.getBuildingBulk().replace("\"", "\\\"") : null;
    }

    public String getApartmentNumber() {
        return group.getApartmentNumber() != null ? group.getApartmentNumber().replace("\"", "\\\"") : null;
    }

    public String getLastName() {
        return group.getLastName() != null ? group.getLastName().replace("\"", "\\\"") : null;
    }

    public String getMiddleName() {
        return group.getMiddleName() != null ? group.getMiddleName().replace("\"", "\\\"") : null;
    }

    public String getFirstName() {
        return group.getFirstName() != null ? group.getFirstName().replace("\"", "\\\"") : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public final void setName(ClassToTypeRegistry typeRegistry) {

        Integer errorType = group.getErrorType();

        StringBuilder name = new StringBuilder();

        appendStreetType(name);
        appendStreetName(name);

        if (errorType == typeRegistry.getType(Street.class)) {

        } else if (errorType == typeRegistry.getType(StreetType.class)) {

        } else if (errorType == typeRegistry.getType(BuildingAddress.class)) {
            appendBuildingNumber(name);
            appendBuildingBulk(name);
        } else if (errorType == typeRegistry.getType(Apartment.class)) {
            appendBuildingNumber(name);
            appendBuildingBulk(name);
            appendApartmentNumber(name);
        } else if (errorType == typeRegistry.getType(Person.class)) {
            appendBuildingNumber(name);
            appendBuildingBulk(name);
            appendApartmentNumber(name);
            appendLastName(name);
            appendFirstName(name);
            appendMiddleName(name);
        }
        this.name = name.toString().trim();

    }

    private void appendStreetType(StringBuilder name) {
        if (isNotEmpty(group.getStreetType())) {
            name.append(group.getStreetType()).append(".");
        }
    }

    private void appendStreetName(StringBuilder name) {
        if (isNotEmpty(group.getStreetName())) {
            name.append(" ").append(group.getStreetName());
        }
    }

    private void appendBuildingNumber(StringBuilder name) {
        if (isNotEmpty(group.getBuildingNumber())) {
            name.append(", ").append(group.getBuildingNumber());
        }
    }

    private void appendBuildingBulk(StringBuilder name) {
        if (isNotEmpty(group.getBuildingBulk())) {
            name.append(" ").append(group.getBuildingBulk());
        }
    }

    private void appendApartmentNumber(StringBuilder name) {
        if (isNotEmpty(group.getApartmentNumber())) {
            name.append(", ").append(group.getApartmentNumber());
        }
    }

    private void appendLastName(StringBuilder name) {
        if (isNotEmpty(group.getLastName())) {
            name.append(", ").append(group.getLastName());
        }
    }

    private void appendFirstName(StringBuilder name) {
        if (isNotEmpty(group.getFirstName())) {
            name.append(" ").append(group.getFirstName());
        }
    }

    private void appendMiddleName(StringBuilder name) {
        if (isNotEmpty(group.getMiddleName())) {
            name.append(" ").append(group.getMiddleName());
        }
    }

    public String getCriteria(ClassToTypeRegistry typeRegistry) {

        String isNull = " is ? ";
        String value = " = ? ";

        String whereTownName = " and town_name" + (group.getTownName() == null ? isNull : value);
        String whereStreetType = " and street_type" + (group.getStreetType() == null ? isNull : value);
        String whereStreetName = " and street_name" + (group.getStreetName() == null ? isNull : value);
        String whereBuildingNumber = " and building_number" + (group.getBuildingNumber() == null ? isNull : value);
        String whereBuildingBulk = " and bulk_number" + (group.getBuildingBulk() == null ? isNull : value);
        String whereApartmentNumber = " and apartment_number" + (group.getApartmentNumber() == null ? isNull : value);
        String whereLastName = " and last_name" + (group.getLastName() == null ? isNull : value);
        String whereFirstName = " and first_name" + (group.getFirstName() == null ? isNull : value);
        String whereMiddleName = " and middle_name" + (group.getMiddleName() == null ? isNull : value);

        StringBuilder criteria = new StringBuilder(whereTownName);

        if (group.getErrorType() == typeRegistry.getType(Street.class)) {
            criteria.append(whereStreetType).append(whereStreetName);
        } else if (group.getErrorType() == typeRegistry.getType(StreetType.class)) {
            criteria.append(whereStreetType).append(whereStreetName);
        } else if (group.getErrorType() == typeRegistry.getType(BuildingAddress.class)) {
            criteria.append(whereStreetType).append(whereStreetName).append(whereBuildingNumber).append(whereBuildingBulk);
        } else if (group.getErrorType() == typeRegistry.getType(Apartment.class)) {
            criteria.append(whereStreetType).append(whereStreetName).append(whereBuildingNumber).append(whereBuildingBulk).append(whereApartmentNumber);
        } else if (group.getErrorType() == typeRegistry.getType(Person.class)) {
            criteria.append(whereStreetType).append(whereStreetName).append(whereBuildingNumber).append(whereBuildingBulk).append(whereApartmentNumber).append(whereLastName).append(whereFirstName).append(whereMiddleName);
        }

        return criteria.toString();

    }

    public List<Object> getParams(ClassToTypeRegistry typeRegistry) {

        List<Object> params = list();

        params.add(group.getTownName());

        if (group.getErrorType() == typeRegistry.getType(Street.class)) {
            params.add(group.getStreetType());
            params.add(group.getStreetName());
        } else if (group.getErrorType() == typeRegistry.getType(StreetType.class)) {
            params.add(group.getStreetType());
            params.add(group.getStreetName());
        } else if (group.getErrorType() == typeRegistry.getType(BuildingAddress.class)) {
            params.add(group.getStreetType());
            params.add(group.getStreetName());
            params.add(group.getBuildingNumber());
            params.add(group.getBuildingBulk());
        } else if (group.getErrorType() == typeRegistry.getType(Apartment.class)) {
            params.add(group.getStreetType());
            params.add(group.getStreetName());
            params.add(group.getBuildingNumber());
            params.add(group.getBuildingBulk());
            params.add(group.getApartmentNumber());
        } else if (group.getErrorType() == typeRegistry.getType(Person.class)) {
            params.add(group.getStreetType());
            params.add(group.getStreetName());
            params.add(group.getBuildingNumber());
            params.add(group.getBuildingBulk());
            params.add(group.getApartmentNumber());
            params.add(group.getLastName());
            params.add(group.getFirstName());
            params.add(group.getMiddleName());
        }

        return params;

    }

    public RecordErrorsGroup getGroup() {
        return group;
    }

    public void setGroup(RecordErrorsGroup group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("name", name).
                append("group", group).
                toString();
    }
}
