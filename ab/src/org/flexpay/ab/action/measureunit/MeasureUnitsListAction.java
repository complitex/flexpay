package org.flexpay.ab.action.measureunit;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.service.MeasureUnitService;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class MeasureUnitsListAction extends FPActionSupport {

	private List<MeasureUnit> units = list();

	private MeasureUnitService measureUnitService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		units = measureUnitService.find();

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public List<MeasureUnit> getUnits() {
		return units;
	}

	@Required
	public void setMeasureUnitService(MeasureUnitService measureUnitService) {
		this.measureUnitService = measureUnitService;
	}

}
