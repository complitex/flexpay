package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.ProcessingStatus;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.DiffService;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.persistence.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class OrganizationHistoryGenerator implements HistoryGenerator<Organization> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private OrganizationService organizationService;
	private OrganizationHistoryBuilder historyBuilder;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull Organization obj) {

		if (diffService.hasDiffs(obj)) {
			log.debug("Identity type already has history, do nothing {}", obj);
			return;
		}

		Organization org = organizationService.readFull(stub(obj));
		if (org == null) {
			log.warn("Requested organization history generation, but not found: {}", obj);
			return;
		}

		Diff diff = historyBuilder.diff(null, org);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setHistoryBuilder(OrganizationHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}
}
