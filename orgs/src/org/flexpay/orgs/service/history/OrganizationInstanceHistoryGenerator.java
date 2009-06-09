package org.flexpay.orgs.service.history;

import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.flexpay.orgs.persistence.OrganizationInstance;
import org.flexpay.orgs.persistence.OrganizationInstanceDescription;
import org.flexpay.orgs.service.OrganizationInstanceService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrganizationInstanceHistoryGenerator<
		D extends OrganizationInstanceDescription,
		T extends OrganizationInstance<D, T>> implements HistoryGenerator<T> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private OrganizationInstanceService<D, T> instanceService;
	private OrganizationInstanceHistoryBuilder<D, T> historyBuilder;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull T obj) {

		if (diffService.hasDiffs(obj)) {
			log.debug("Instance already has history, do nothing {}", obj);
			return;
		}

		T org = instanceService.read(stub(obj));
		if (org == null) {
			log.warn("Requested organization instance history generation, but not found: {}", obj);
			return;
		}

		Diff diff = historyBuilder.diff(null, org);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);
	}

	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	public void setInstanceService(OrganizationInstanceService<D, T> instanceService) {
		this.instanceService = instanceService;
	}

	public void setHistoryBuilder(OrganizationInstanceHistoryBuilder<D, T> historyBuilder) {
		this.historyBuilder = historyBuilder;
	}
}
