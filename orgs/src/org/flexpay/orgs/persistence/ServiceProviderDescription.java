package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;

public class ServiceProviderDescription extends OrganizationInstanceDescription {

	public ServiceProviderDescription() {
	}

	public ServiceProviderDescription(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}
}
