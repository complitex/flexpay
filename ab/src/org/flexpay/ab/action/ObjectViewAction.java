package org.flexpay.ab.action;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.*;
import org.flexpay.common.service.NameTimeDependentService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public abstract class ObjectViewAction<
		TV extends TemporaryValue<TV>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>,
		T extends Translation> extends FPActionSupport {

	protected NTD object;

	protected NameTimeDependentService<TV, DI, NTD, T> nameTimeDependentService;

	@NotNull
	@Override
	public String doExecute() {

		if (object == null || object.isNew()) {
			log.warn("Incorrect object id");
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}

		Stub<NTD> stub = stub(object);
		object = nameTimeDependentService.readFull(stub);

		if (object == null) {
			log.warn("Can't get object with id {} from DB", stub.getId());
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		} else if (object.isNotActive()) {
			log.warn("Object with id {} is disabled", stub.getId());
			addActionError(getText("common.object_not_selected"));
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

	public NTD getObject() {
		return object;
	}

	public void setObject(NTD object) {
		this.object = object;
	}

	@Required
	public void setNameTimeDependentService(NameTimeDependentService<TV, DI, NTD, T> nameTimeDependentService) {
		this.nameTimeDependentService = nameTimeDependentService;
	}

}
