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
import org.springframework.beans.factory.annotation.Required;

public class OrganizationInstanceHistoryGenerator<
		D extends OrganizationInstanceDescription,
		T extends OrganizationInstance<D, T>> implements HistoryGenerator<T> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private OrganizationInstanceService<D, T> instanceService;
	private OrganizationInstanceHistoryBuilder<D, T> historyBuilder;
	private OrganizationInstanceReferencesHistoryGenerator<D, T> referencesHistoryGenerator;

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

		referencesHistoryGenerator.generateReferencesHistory(org);

		if (!diffService.hasDiffs(org)) {
			Diff diff = historyBuilder.diff(null, org);
			diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			diffService.create(diff);
		}
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setInstanceService(OrganizationInstanceService<D, T> instanceService) {
		this.instanceService = instanceService;
	}

	@Required
	public void setHistoryBuilder(OrganizationInstanceHistoryBuilder<D, T> historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setReferencesHistoryGenerator(OrganizationInstanceReferencesHistoryGenerator<D, T> referencesHistoryGenerator) {
		this.referencesHistoryGenerator = referencesHistoryGenerator;
	}
}
