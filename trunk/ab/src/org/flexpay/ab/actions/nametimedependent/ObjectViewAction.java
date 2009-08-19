package org.flexpay.ab.actions.nametimedependent;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.persistence.NameDateInterval;
import org.flexpay.common.persistence.NameTimeDependentChild;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.TemporaryValue;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

public abstract class ObjectViewAction<
		TV extends TemporaryValue<TV>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>,
		T extends Translation> extends ActionBase<TV, DI, NTD, T> {

	protected NTD object;

	@NotNull
	@Override
	public String doExecute() {

		log.info("Object: {}", object);
		if (object.isNew()) {
			addActionError(getText("error.no_id"));
			return REDIRECT_ERROR;
		}

		object = nameTimeDependentService.readFull(stub(object));
		if (object == null) {
			addActionError(getText("error.invalid_id"));
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

	@Override
	protected ArrayStack getFilters() {
		return null;
	}

	@Override
	protected void setFilters(ArrayStack filters) {
	}

}
