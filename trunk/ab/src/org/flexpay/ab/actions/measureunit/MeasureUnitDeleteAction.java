package org.flexpay.ab.actions.measureunit;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.MeasureUnitService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MeasureUnitDeleteAction extends FPActionSupport {

	private MeasureUnitService measureUnitService;

	private List<Long> objectIds;

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

		if (log.isDebugEnabled()) {
			log.debug("Deleting measure units: " + objectIds);
		}

		for (Long id : objectIds) {
			MeasureUnit unit = measureUnitService.read(new Stub<MeasureUnit>(id));
			if (unit != null) {
				unit.disable();
				measureUnitService.save(unit);
			}
		}

		return REDIRECT_SUCCESS;
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
		return REDIRECT_SUCCESS;
	}

	public List<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(List<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public void setMeasureUnitService(MeasureUnitService measureUnitService) {
		this.measureUnitService = measureUnitService;
	}
}
