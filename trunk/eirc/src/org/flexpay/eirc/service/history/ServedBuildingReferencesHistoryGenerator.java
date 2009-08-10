package org.flexpay.eirc.service.history;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.history.BuildingReferencesHistoryGenerator;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.persistence.ServiceOrganizationDescription;
import org.flexpay.orgs.service.history.OrganizationInstanceHistoryGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class ServedBuildingReferencesHistoryGenerator extends BuildingReferencesHistoryGenerator {

	private OrganizationInstanceHistoryGenerator<
			ServiceOrganizationDescription, ServiceOrganization> serviceOrganizationHistoryGenerator;

	@Override
	public void generateReferencesHistory(@NotNull Building obj) {
		super.generateReferencesHistory(obj);

		ServedBuilding servedBuilding = (ServedBuilding) obj;
		ServiceOrganization organization = servedBuilding.getServiceOrganization();
		if (organization != null) {
			serviceOrganizationHistoryGenerator.generateFor(organization);
		}
	}

	@Required
	public void setServiceOrganizationHistoryGenerator(
			OrganizationInstanceHistoryGenerator<
					ServiceOrganizationDescription, ServiceOrganization> serviceOrganizationHistoryGenerator) {

		this.serviceOrganizationHistoryGenerator = serviceOrganizationHistoryGenerator;
	}
}
