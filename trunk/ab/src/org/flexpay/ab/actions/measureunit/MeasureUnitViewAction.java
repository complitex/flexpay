package org.flexpay.ab.actions.measureunit;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.MeasureUnit;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.MeasureUnitService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class MeasureUnitViewAction extends FPActionSupport {

	private MeasureUnit measureUnit = new MeasureUnit();

	private MeasureUnitService measureUnitService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (measureUnit.isNew()) {
			log.error(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}

		measureUnit = measureUnitService.readFull(stub(measureUnit));

		if (measureUnit == null) {
			log.error(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		}

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
		return REDIRECT_ERROR;
	}

	public MeasureUnit getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(MeasureUnit measureUnit) {
		this.measureUnit = measureUnit;
	}

	@Required
	public void setMeasureUnitService(MeasureUnitService measureUnitService) {
		this.measureUnitService = measureUnitService;
	}

}
