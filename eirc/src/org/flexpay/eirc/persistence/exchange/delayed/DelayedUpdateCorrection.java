package org.flexpay.eirc.persistence.exchange.delayed;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.persistence.exchange.DelayedUpdate;

public class DelayedUpdateCorrection implements DelayedUpdate {

	private CorrectionsService service;
	private DomainObject object;
	private String externalId;
	private Stub<DataSourceDescription> sd;

	public DelayedUpdateCorrection(CorrectionsService service, DomainObject object,
								   String externalId, Stub<DataSourceDescription> sd) {
		this.service = service;
		this.object = object;
		this.externalId = externalId;
		this.sd = sd;
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
		service.save(service.getStub(externalId, object, sd));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DelayedUpdateCorrection that = (DelayedUpdateCorrection) o;

		if (externalId != null ? !externalId.equals(that.externalId) : that.externalId != null) return false;
		if (object != null ? !object.equals(that.object) : that.object != null) return false;
		if (sd != null ? !sd.equals(that.sd) : that.sd != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = object != null ? object.hashCode() : 0;
		result = 31 * result + (externalId != null ? externalId.hashCode() : 0);
		result = 31 * result + (sd != null ? sd.hashCode() : 0);
		return result;
	}
}
