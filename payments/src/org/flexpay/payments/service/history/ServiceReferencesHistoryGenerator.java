package org.flexpay.payments.service.history;

import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.flexpay.common.service.history.MeasureUnitHistoryGenerator;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.ServiceProviderDescription;
import org.flexpay.orgs.service.history.OrganizationInstanceHistoryGenerator;
import org.flexpay.payments.persistence.Service;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class ServiceReferencesHistoryGenerator implements ReferencesHistoryGenerator<Service> {

	private MeasureUnitHistoryGenerator measureUnitHistoryGenerator;
	private OrganizationInstanceHistoryGenerator<ServiceProviderDescription, ServiceProvider> providerHistoryGenerator;

	@Override
	public void generateReferencesHistory(@NotNull Service obj) {
		measureUnitHistoryGenerator.generateFor(obj.getMeasureUnit());
		providerHistoryGenerator.generateFor(obj.getServiceProvider());
	}

	@Required
	public void setMeasureUnitHistoryGenerator(MeasureUnitHistoryGenerator measureUnitHistoryGenerator) {
		this.measureUnitHistoryGenerator = measureUnitHistoryGenerator;
	}

	@Required
	public void setProviderHistoryGenerator(
			OrganizationInstanceHistoryGenerator<ServiceProviderDescription, ServiceProvider> providerHistoryGenerator) {
		this.providerHistoryGenerator = providerHistoryGenerator;
	}
}
