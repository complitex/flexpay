package org.flexpay.common.service.history;

import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.flexpay.common.service.MeasureUnitService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.Collection;

import static org.flexpay.common.persistence.Stub.stub;

public class MeasureUnitHistoryGenerator implements HistoryGenerator<MeasureUnit> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private MeasureUnitHistoryBuilder historyBuilder;
	private MeasureUnitService unitService;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	@Override
	public void generateFor(@NotNull MeasureUnit obj) {

		if (diffService.hasDiffs(obj)) {
			log.debug("MeasureUnit already has history, do nothing {}", obj);
			return;
		}

		MeasureUnit type = unitService.readFull(stub(obj));
		if (type == null) {
			log.warn("Requested MeasureUnit history generation, but not found: {}", obj);
			return;
		}

		Diff diff = historyBuilder.diff(null, type);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);
	}

	@Override
	public void generateFor(@NotNull Collection<MeasureUnit> objs) {
		for (MeasureUnit unit : objs) {
			generateFor(unit);
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
	public void setHistoryBuilder(MeasureUnitHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setUnitService(MeasureUnitService unitService) {
		this.unitService = unitService;
	}

}
