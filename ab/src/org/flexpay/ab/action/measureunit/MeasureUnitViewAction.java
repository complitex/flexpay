package org.flexpay.ab.action.measureunit;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.MeasureUnitService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class MeasureUnitViewAction extends FPActionSupport {

	private MeasureUnit measureUnit = new MeasureUnit();

	private MeasureUnitService measureUnitService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (measureUnit == null || measureUnit.isNew()) {
			log.warn("Incorrect measure unit id");
			addActionError(getText("common.error.measure_unit.incorrect_measure_unit_id"));
			return REDIRECT_ERROR;
		}

		Stub<MeasureUnit> stub = stub(measureUnit);
		measureUnit = measureUnitService.readFull(stub);

		if (measureUnit == null) {
			log.warn("Can't get measure unit with id {} from DB", stub.getId());
			addActionError(getText("common.error.measure_unit.cant_get_measure_unit"));
			return REDIRECT_ERROR;
		} else if (measureUnit.isNotActive()) {
			log.warn("Measure unit with id {} is disabled", stub.getId());
			addActionError(getText("common.error.measure_unit.cant_get_measure_unit"));
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
