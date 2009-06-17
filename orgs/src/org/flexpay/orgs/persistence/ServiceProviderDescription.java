package org.flexpay.orgs.persistence;

import org.jetbrains.annotations.NotNull;
import org.flexpay.common.persistence.Language;

public class ServiceProviderDescription extends OrganizationInstanceDescription {

	public ServiceProviderDescription() {
	}

	public ServiceProviderDescription(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}
}
