package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.flexpay.orgs.persistence.OrganizationInstance;
import org.flexpay.orgs.persistence.OrganizationInstanceDescription;
import org.jetbrains.annotations.NotNull;

public class OrganizationInstanceReferencesHistoryGenerator<
		D extends OrganizationInstanceDescription,
		T extends OrganizationInstance<D, T>> implements ReferencesHistoryGenerator<T> {

	private OrganizationHistoryGenerator organizationHistoryGenerator;

	@Override
	public void generateReferencesHistory(@NotNull T obj) {
		organizationHistoryGenerator.generateFor(obj.getOrganization());
	}

	public void setOrganizationHistoryGenerator(OrganizationHistoryGenerator organizationHistoryGenerator) {
		this.organizationHistoryGenerator = organizationHistoryGenerator;
	}
}
