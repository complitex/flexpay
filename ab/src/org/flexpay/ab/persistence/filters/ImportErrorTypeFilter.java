package org.flexpay.ab.persistence.filters;


import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.ab.persistence.*;

import java.util.Map;
import java.util.TreeMap;

public class ImportErrorTypeFilter {

	private Integer selectedType = -1;
	protected Map<Integer, String> errorTypes = new TreeMap<Integer, String>();

	public ImportErrorTypeFilter(ClassToTypeRegistry typeRegistry) {
		errorTypes.put(typeRegistry.getType(Street.class), "import.error_type.street");
		errorTypes.put(typeRegistry.getType(StreetType.class), "import.error_type.street_type");
		errorTypes.put(typeRegistry.getType(Buildings.class), "import.error_type.building");
		errorTypes.put(typeRegistry.getType(Apartment.class), "import.error_type.apartment");
		errorTypes.put(typeRegistry.getType(Person.class), "import.error_type.person");
	}

	public Integer getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(Integer selectedType) {
		this.selectedType = selectedType;
	}

	public Map<Integer, String> getErrorTypes() {
		return errorTypes;
	}

	public void setErrorTypes(Map<Integer, String> errorTypes) {
		this.errorTypes = errorTypes;
	}
}