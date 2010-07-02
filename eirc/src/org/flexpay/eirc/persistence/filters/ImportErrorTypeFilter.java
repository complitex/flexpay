package org.flexpay.eirc.persistence.filters;

import org.flexpay.common.service.importexport.ClassToTypeRegistry;

public class ImportErrorTypeFilter extends org.flexpay.ab.persistence.filters.ImportErrorTypeFilter {

    @Override
	public void init(ClassToTypeRegistry typeRegistry) {
		super.init(typeRegistry);
	}
}
