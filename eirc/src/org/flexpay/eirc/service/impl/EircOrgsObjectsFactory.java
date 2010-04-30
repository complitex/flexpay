package org.flexpay.eirc.service.impl;

import org.flexpay.eirc.persistence.EircServiceOrganization;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.service.OrgsObjectsFactory;
import org.jetbrains.annotations.NotNull;

public class EircOrgsObjectsFactory implements OrgsObjectsFactory {

	@NotNull
	@Override
	public ServiceOrganization newServiceOrganization() {
		return EircServiceOrganization.newInstance();
	}
}
