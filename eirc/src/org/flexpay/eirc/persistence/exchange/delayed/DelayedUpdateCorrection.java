package org.flexpay.eirc.persistence.exchange.delayed;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.persistence.exchange.DelayedUpdate;
import org.jetbrains.annotations.NotNull;

public class DelayedUpdateCorrection implements DelayedUpdate {

	private CorrectionsService service;
	private DomainObject object;
	private String externalId;
	private Stub<DataSourceDescription> sd;

	public DelayedUpdateCorrection(@NotNull CorrectionsService service, @NotNull DomainObject object,
								   @NotNull String externalId, @NotNull Stub<DataSourceDescription> sd) {
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

	public DomainObject getObject() {
		return object;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DelayedUpdateCorrection that = (DelayedUpdateCorrection) o;

		if (!externalId.equals(that.externalId)) return false;
		if (!object.getClass().equals(that.object.getClass())) return false;
		if (!sd.equals(that.sd)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = object.hashCode();
		result = 31 * result + externalId.hashCode();
		result = 31 * result + sd.hashCode();
		return result;
	}
}
