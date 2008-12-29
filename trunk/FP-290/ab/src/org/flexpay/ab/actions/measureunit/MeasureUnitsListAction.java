package org.flexpay.ab.actions.measureunit;

import org.flexpay.common.service.MeasureUnitService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.MeasureUnit;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Collections;

public class MeasureUnitsListAction extends FPActionSupport {

	private MeasureUnitService measureUnitService;

	private List<MeasureUnit> units = Collections.emptyList();

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	protected String doExecute() throws Exception {
		units = measureUnitService.listUnits();
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
	protected String getErrorResult() {
		return SUCCESS;
	}

	public List<MeasureUnit> getUnits() {
		return units;
	}

	public void setMeasureUnitService(MeasureUnitService measureUnitService) {
		this.measureUnitService = measureUnitService;
	}
}
