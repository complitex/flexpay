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
}
