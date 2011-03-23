package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.flexpay.orgs.persistence.Cashbox;
import org.jetbrains.annotations.NotNull;

public class CashboxReferencesHistoryGenerator implements ReferencesHistoryGenerator<Cashbox> {

	private PaymentPointHistoryGenerator pointHistoryGenerator;

	@Override
	public void generateReferencesHistory(@NotNull Cashbox obj) {
		pointHistoryGenerator.generateFor(obj.getPaymentPoint());
	}

	public void setPointHistoryGenerator(PaymentPointHistoryGenerator pointHistoryGenerator) {
		this.pointHistoryGenerator = pointHistoryGenerator;
	}
}
