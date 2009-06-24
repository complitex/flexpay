package org.flexpay.orgs.service;

import org.flexpay.orgs.persistence.ServiceOrganization;
import org.jetbrains.annotations.NotNull;

/**
 * Persistence objects factory. Used to handle module dependent objects creation as all module object should be be of
 * the same type
 */
public interface OrgsObjectsFactory {

	@NotNull
	ServiceOrganization newServiceOrganization();
}
