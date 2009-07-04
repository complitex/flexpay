package org.flexpay.eirc.actions.registry;

import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;

public class RegistryViewAction extends org.flexpay.payments.actions.registry.RegistryViewAction {
    public ImportErrorTypeFilter getImportErrorTypeFilter() {
		if (importErrorTypeFilter == null) {
            importErrorTypeFilter = new org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter();
        }
        return importErrorTypeFilter;
	}
}
