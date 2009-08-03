package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.orgs.persistence.PaymentsCollectorDescription;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class PaymentPointReferencesHistoryGenerator implements ReferencesHistoryGenerator<PaymentPoint> {

	private OrganizationInstanceHistoryGenerator<PaymentsCollectorDescription, PaymentsCollector> collectorHistoryGenerator;

	@Override
	public void generateReferencesHistory(@NotNull PaymentPoint obj) {
		collectorHistoryGenerator.generateFor(obj.getCollector());
	}

	@Required
	public void setCollectorHistoryGenerator(
			OrganizationInstanceHistoryGenerator<PaymentsCollectorDescription, PaymentsCollector> collectorHistoryGenerator) {
		this.collectorHistoryGenerator = collectorHistoryGenerator;
	}
}
