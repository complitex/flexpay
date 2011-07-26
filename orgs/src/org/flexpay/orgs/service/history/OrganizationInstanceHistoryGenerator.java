package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.orgs.persistence.OrganizationInstance;
import org.flexpay.orgs.persistence.OrganizationInstanceDescription;
import org.flexpay.orgs.service.OrganizationInstanceService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.Collection;

import static org.flexpay.common.persistence.Stub.stub;

public class OrganizationInstanceHistoryGenerator<
		D extends OrganizationInstanceDescription,
		T extends OrganizationInstance<D, T>> implements HistoryGenerator<T> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private OrganizationInstanceService<D, T> instanceService;
	private OrganizationInstanceHistoryBuilder<D, T> historyBuilder;
	private OrganizationInstanceReferencesHistoryGenerator<D, T> referencesHistoryGenerator;
	private SessionUtils sessionUtils;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
    @Override
	public void generateFor(@NotNull T obj) {

		if (diffService.hasDiffs(obj)) {
			log.debug("Instance already has history, do nothing {}", obj.getId());
			return;
		}

		T org = instanceService.read(stub(obj));
		sessionUtils.evict(org);
		if (org == null) {
			log.warn("Requested organization instance history generation, but not found: {}", obj.getId());
			return;
		}

		referencesHistoryGenerator.generateReferencesHistory(org);

		Diff diff = historyBuilder.diff(null, org);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);
	}

	@Override
	public void generateFor(@NotNull Collection<T> objs) {
		for (T t : objs) {
			generateFor(t);
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

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}
}
