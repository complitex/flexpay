package org.flexpay.eirc.actions.registry;

import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;

public class RegistryViewAction extends org.flexpay.payments.actions.registry.RegistryViewAction {
    private boolean setupCompleted;

    @Override
    public String execute() throws Exception {
        if (setupCompleted) {
            addActionMessage(getText("eirc.setup_completed"));
        }
        return super.execute();
    }

    public ImportErrorTypeFilter getImportErrorTypeFilter() {
		if (importErrorTypeFilter == null) {
            importErrorTypeFilter = new org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter();
        }
        return importErrorTypeFilter;
	}

    public void setSetupCompleted(boolean setupCompleted) {
        this.setupCompleted = setupCompleted;
    }
}
