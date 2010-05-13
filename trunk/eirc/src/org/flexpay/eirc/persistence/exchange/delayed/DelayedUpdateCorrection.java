package org.flexpay.eirc.persistence.exchange.delayed;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.persistence.exchange.DelayedUpdate;

public class DelayedUpdateCorrection implements DelayedUpdate {

	private CorrectionsService service;
	private DataCorrection dc;

	public DelayedUpdateCorrection(CorrectionsService service, DomainObject object,
								   String externalId, Stub<DataSourceDescription> sd) {
		this.service = service;
		this.dc = service.getStub(externalId, object, sd);
	}

	/**
	 * Perform storage update
	 *
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if operation fails
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if operation fails
	 */
	@Override
	public void doUpdate() throws FlexPayException, FlexPayExceptionContainer {
		service.save(dc);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DelayedUpdateCorrection that = (DelayedUpdateCorrection) o;

		if (dc != null ? !dc.equals(that.dc) : that.dc != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return dc != null ? dc.hashCode() : 0;
	}
}
