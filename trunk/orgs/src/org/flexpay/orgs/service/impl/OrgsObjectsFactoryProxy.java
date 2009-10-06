package org.flexpay.orgs.service.impl;

import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.service.OrgsObjectsFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class OrgsObjectsFactoryProxy implements OrgsObjectsFactory {

	private OrgsObjectsFactoryHolder factoryHolder;

	@NotNull
	@Override
	public ServiceOrganization newServiceOrganization() {
		return factoryHolder.getInstance().newServiceOrganization();
	}

	@Required
	public void setFactoryHolder(OrgsObjectsFactoryHolder factoryHolder) {
		this.factoryHolder = factoryHolder;
	}
}
