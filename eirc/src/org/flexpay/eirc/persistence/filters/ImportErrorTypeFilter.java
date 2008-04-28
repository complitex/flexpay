package org.flexpay.eirc.persistence.filters;

import org.flexpay.ab.persistence.Person;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;

public class ImportErrorTypeFilter extends org.flexpay.ab.persistence.filters.ImportErrorTypeFilter {

	public ImportErrorTypeFilter(ClassToTypeRegistry typeRegistry) {
		super(typeRegistry);

		errorTypes.put(typeRegistry.getType(Person.class), "import.error_type.person");
	}
}