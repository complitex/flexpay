package org.flexpay.eirc.service.imp;

import org.flexpay.orgs.service.OrgsObjectsFactory;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.eirc.persistence.EircServiceOrganization;
import org.jetbrains.annotations.NotNull;

public class EircOrgsObjectsFactory implements OrgsObjectsFactory {

	@NotNull
	@Override
	public ServiceOrganization newServiceOrganization() {
		return EircServiceOrganization.newInstance();
	}
}
