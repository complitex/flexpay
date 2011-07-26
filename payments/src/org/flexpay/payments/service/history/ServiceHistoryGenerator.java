package org.flexpay.payments.service.history;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.Collection;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

public class ServiceHistoryGenerator implements HistoryGenerator<Service> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private SPService spService;
	private ServiceHistoryBuilder historyBuilder;
	private ServiceReferencesHistoryGenerator referencesHistoryGenerator;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
    @Override
	public void generateFor(@NotNull Service obj) {

		Service service = spService.readFull(stub(obj));
		if (service == null) {
			log.warn("Requested service history generation, but not found: {}", obj);
			return;
		}

		generateForSingle(service);
	}

	private void generateForSingle(Service service) {
		referencesHistoryGenerator.generateReferencesHistory(service);

		if (!diffService.hasDiffs(service)) {
			Diff diff = historyBuilder.diff(null, service);
			diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			diffService.create(diff);
		}
	}

	@Override
	public void generateFor(@NotNull Collection<Service> objs) {

		List<Service> services = spService.readFull(DomainObject.collectionIds(objs), false);
		for (Service service : services) {
			generateForSingle(service);
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
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	@Required
	public void setHistoryBuilder(ServiceHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setReferencesHistoryGenerator(ServiceReferencesHistoryGenerator referencesHistoryGenerator) {
		this.referencesHistoryGenerator = referencesHistoryGenerator;
	}
}
