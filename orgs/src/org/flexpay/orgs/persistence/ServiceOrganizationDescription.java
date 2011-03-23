package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;

public class ServiceOrganizationDescription extends OrganizationInstanceDescription {

	public ServiceOrganizationDescription() {
	}

	public ServiceOrganizationDescription(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}
}
