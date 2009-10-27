package org.flexpay.ab.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.NameDateInterval;
import org.flexpay.common.persistence.NameTimeDependentChild;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.TemporaryValue;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.service.NameTimeDependentService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

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

		log.debug("Object: {}", object);
		if (object.isNew()) {
			addActionError(getText("error.no_id"));
			return REDIRECT_ERROR;
		}

		object = nameTimeDependentService.readFull(stub(object));
		if (object == null) {
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
