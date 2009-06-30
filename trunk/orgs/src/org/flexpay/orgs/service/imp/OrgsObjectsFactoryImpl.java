package org.flexpay.orgs.service.imp;

import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.service.OrgsObjectsFactory;
import org.jetbrains.annotations.NotNull;

public class OrgsObjectsFactoryImpl implements OrgsObjectsFactory {

	@NotNull
	@Override
	public ServiceOrganization newServiceOrganization() {
		return ServiceOrganization.newInstance();
	}

}
