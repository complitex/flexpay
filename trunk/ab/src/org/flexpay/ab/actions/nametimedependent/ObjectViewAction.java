package org.flexpay.ab.actions.nametimedependent;

import com.opensymphony.xwork2.Preparable;
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
		T extends Translation> extends ActionBase<TV, DI, NTD, T> implements Preparable {

	public static final String ATTRIBUTE_OBJECT = ObjectViewAction.class.getName() + ".OBJECT";

	private NTD object;

	/**
	 * This method is called to allow the action to prepare itself.
	 *
	 * @throws Exception thrown if a system level exception occurs.
	 */
	@SuppressWarnings ({"unchecked"})
	public void prepare() throws Exception {
		log.info("Object: {}", object);
		if (object.getId() == null) {
			NTD reg = (NTD) session.remove(ATTRIBUTE_OBJECT);
			if (reg != null) {
				object = reg;
			}
		}
	}

	@NotNull
	public String doExecute() {

		log.info("Object: {}", object);
		if (object.getId() != null) {
			object = nameTimeDependentService.readFull(stub(object));
			return SUCCESS;
		} else {
			addActionError(getText("error.no_id"));
			return REDIRECT_ERROR;
		}
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
		return REDIRECT_ERROR;
	}

	public NTD getObject() {
		return object;
	}

	public void setObject(NTD object) {
		this.object = object;
	}

	protected ArrayStack getFilters() {
		return null;
	}

	protected void setFilters(ArrayStack filters) {
	}

}
