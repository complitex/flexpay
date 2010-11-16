package org.flexpay.ab.persistence.filters;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;

public class ImportErrorTypeFilter extends org.flexpay.common.persistence.filter.ImportErrorTypeFilter {

    @Override
	public void init(ClassToTypeRegistry typeRegistry) {
		errorTypes.put(typeRegistry.getType(Street.class), "ab.import.error_type.street");
		errorTypes.put(typeRegistry.getType(StreetType.class), "ab.import.error_type.street_type");
		errorTypes.put(typeRegistry.getType(BuildingAddress.class), "ab.import.error_type.building");
		errorTypes.put(typeRegistry.getType(Apartment.class), "ab.import.error_type.apartment");
		errorTypes.put(typeRegistry.getType(Person.class), "ab.import.error_type.person");
        setGroupByString(typeRegistry);
	}

    private void setGroupByString(ClassToTypeRegistry typeRegistry) {

        StringBuilder groupBy = new StringBuilder(" group by town_name ");

        if (selectedType == typeRegistry.getType(StreetType.class)) {
            groupBy.append(", street_type ");
        } else if (selectedType == typeRegistry.getType(Street.class)) {
            groupBy.append(", street_type, street_name");
        } else if (selectedType == typeRegistry.getType(BuildingAddress.class)) {
            groupBy.append(", street_type, street_name, building_number, bulk_number ");
        } else if (selectedType == typeRegistry.getType(Apartment.class)) {
            groupBy.append(", street_type, street_name, building_number, bulk_number, apartment_number ");
        } else if (selectedType == typeRegistry.getType(Person.class)) {
            groupBy.append(", street_type, street_name, building_number, bulk_number, apartment_number, last_name, first_name, middle_name ");
        }

        groupByString = groupBy.toString();

    }

}
