package org.flexpay.ab.persistence.filters;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;

public class ImportErrorTypeFilter extends org.flexpay.common.persistence.filter.ImportErrorTypeFilter {

	public void init(ClassToTypeRegistry typeRegistry) {
		errorTypes.put(typeRegistry.getType(Street.class), "import.error_type.street");
		errorTypes.put(typeRegistry.getType(StreetType.class), "import.error_type.street_type");
		errorTypes.put(typeRegistry.getType(BuildingAddress.class), "import.error_type.building");
		errorTypes.put(typeRegistry.getType(Apartment.class), "import.error_type.apartment");
		errorTypes.put(typeRegistry.getType(Person.class), "import.error_type.person");
	}
}
