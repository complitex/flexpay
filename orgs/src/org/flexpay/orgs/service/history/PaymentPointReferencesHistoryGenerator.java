package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentCollectorDescription;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class PaymentPointReferencesHistoryGenerator implements ReferencesHistoryGenerator<PaymentPoint> {

	private OrganizationInstanceHistoryGenerator<PaymentCollectorDescription, PaymentCollector> collectorHistoryGenerator;

	@Override
	public void generateReferencesHistory(@NotNull PaymentPoint obj) {
		collectorHistoryGenerator.generateFor(obj.getCollector());
	}

	@Required
	public void setCollectorHistoryGenerator(
			OrganizationInstanceHistoryGenerator<PaymentCollectorDescription, PaymentCollector> collectorHistoryGenerator) {
		this.collectorHistoryGenerator = collectorHistoryGenerator;
	}
}
