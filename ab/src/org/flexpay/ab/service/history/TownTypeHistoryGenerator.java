package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.Collection;

import static org.flexpay.common.persistence.Stub.stub;

public class TownTypeHistoryGenerator implements HistoryGenerator<TownType> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private TownTypeService townTypeService;
	private DiffService diffService;

	private TownTypeHistoryBuilder historyBuilder;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	@Override
	public void generateFor(@NotNull TownType obj) {

		if (diffService.hasDiffs(obj)) {
			log.info("Town type already has history, do nothing #{}", obj.getId());
			return;
		}

		obj = townTypeService.readFull(stub(obj));

		Diff diff = historyBuilder.diff(null, obj);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);
	}

	@Override
	public void generateFor(@NotNull Collection<TownType> objs) {
		for (TownType townType : objs) {
			generateFor(townType);
		}
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        townTypeService.setJpaTemplate(jpaTemplate);
        diffService.setJpaTemplate(jpaTemplate);
        historyBuilder.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setHistoryBuilder(TownTypeHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

}
