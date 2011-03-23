package org.flexpay.common.service.history;

import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.impl.HistoryHandlerBase;
import org.flexpay.common.service.MeasureUnitService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class MeasureUnitHistoryHandler extends HistoryHandlerBase<MeasureUnit> {

	private MeasureUnitService unitService;

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
	@Override
	public boolean supports(@NotNull Diff diff) {

		return typeRegistry.getType(MeasureUnit.class) == diff.getObjectType();
	}

	/**
	 * Apply diff to the system object
	 *
	 * @param diff Diff
	 * @throws Exception if failure occurs
	 */
	@Override
	public void process(@NotNull Diff diff) throws Exception {

		historyHandlerHelper.process(diff, historyBuilder, unitService, correctionsService);
	}

	@Required
	public void setUnitService(MeasureUnitService unitService) {
		this.unitService = unitService;
	}
}
