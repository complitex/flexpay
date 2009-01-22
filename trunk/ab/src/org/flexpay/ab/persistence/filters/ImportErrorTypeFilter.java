package org.flexpay.ab.persistence.filters;

import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.ab.persistence.*;

import java.util.Map;
import java.util.TreeMap;

public class ImportErrorTypeFilter extends ObjectFilter {

	public static Integer TYPE_NO_ERRORS = 0;
	public static Integer TYPE_ALL = -1;

	private Integer selectedType = TYPE_ALL;
	protected Map<Integer, String> errorTypes = new TreeMap<Integer, String>();

	public ImportErrorTypeFilter() {
		errorTypes.put(TYPE_ALL, "import.error_type.all");
		errorTypes.put(TYPE_NO_ERRORS, "import.error_type.no_error");
	}

	public void init(ClassToTypeRegistry typeRegistry) {
		errorTypes.put(typeRegistry.getType(Street.class), "import.error_type.street");
		errorTypes.put(typeRegistry.getType(StreetType.class), "import.error_type.street_type");
		errorTypes.put(typeRegistry.getType(BuildingAddress.class), "import.error_type.building");
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

	/**
	 * Check if filter selectedType should be applyed when fetching
	 *
	 * @return <code>true</code> if selectedType != {@link #TYPE_ALL}, or <code>false</code> otherwise
	 */
	public boolean needFilter() {
		return !selectedType.equals(TYPE_ALL);
	}

	/**
	 * Check if fetched objects should not gave an error
	 *
	 * @return <code>true</code> if selectedType != {@link #TYPE_NO_ERRORS}, or <code>false</code> otherwise
	 */
	public boolean needFilterWithoutErrors() {
		return selectedType.equals(TYPE_NO_ERRORS);
	}
}
