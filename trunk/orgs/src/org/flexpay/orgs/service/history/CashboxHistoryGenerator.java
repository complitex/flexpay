package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.CashboxService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.Collection;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

public class CashboxHistoryGenerator implements HistoryGenerator<Cashbox> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private CashboxHistoryBuilder historyBuilder;
	private CashboxService cashboxService;
	private CashboxReferencesHistoryGenerator referencesHistoryGenerator;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
    @Override
	public void generateFor(@NotNull Cashbox obj) {

		Cashbox cashbox = cashboxService.read(stub(obj));
		if (cashbox == null) {
			log.warn("Requested cashbox history generation, but not found: #{}", obj.getId());
			return;
		}

		generateForSingle(cashbox);
	}

	private void generateForSingle(Cashbox cashbox) {

		referencesHistoryGenerator.generateReferencesHistory(cashbox);

		if (!diffService.hasDiffs(cashbox)) {
			Diff diff = historyBuilder.diff(null, cashbox);
			diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			diffService.create(diff);
		}
	}

	@Override
	public void generateFor(@NotNull Collection<Cashbox> objs) {
		List<Cashbox> cashboxes = cashboxService.readFull(DomainObject.collectionIds(objs), false);
		for (Cashbox cashbox : cashboxes) {
			generateForSingle(cashbox);
		}
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
    }

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setHistoryBuilder(CashboxHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

	@Required
	public void setReferencesHistoryGenerator(CashboxReferencesHistoryGenerator referencesHistoryGenerator) {
		this.referencesHistoryGenerator = referencesHistoryGenerator;
	}

}
