package org.flexpay.payments.service.history;

import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.Collection;

import static org.flexpay.common.persistence.Stub.stub;

public class ServiceTypeHistoryGenerator implements HistoryGenerator<ServiceType> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private ServiceTypeService typeService;
	private DiffService diffService;
	private ServiceTypeHistoryBuilder historyBuilder;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
    @Override
	public void generateFor(@NotNull ServiceType obj) {

		if (diffService.hasDiffs(obj)) {
			log.debug("Service type already has history, do nothing {}", obj);
			return;
		}

		ServiceType type = typeService.read(stub(obj));
		if (type == null) {
			log.warn("Requested service type history generation, but not found: {}", obj);
			return;
		}

		Diff diff = historyBuilder.diff(null, type);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);
	}

	/**
	 * Do generation of history for several objects
	 *
	 * @param objs Objects to generate history for
	 */
	@Override
	public void generateFor(@NotNull Collection<ServiceType> objs) {
		for (ServiceType serviceType : objs) {
			generateFor(serviceType);
		}
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
    }

	@Required
	public void setTypeService(ServiceTypeService typeService) {
		this.typeService = typeService;
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setHistoryBuilder(ServiceTypeHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

}
