package org.flexpay.orgs.persistence;

import org.jetbrains.annotations.NotNull;
import org.flexpay.common.persistence.Language;

public class ServiceOrganizationDescription extends OrganizationInstanceDescription {

	public ServiceOrganizationDescription() {
	}

	public ServiceOrganizationDescription(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}
}
