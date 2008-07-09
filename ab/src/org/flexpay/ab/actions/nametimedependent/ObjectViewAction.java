package org.flexpay.ab.actions.nametimedependent;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;
import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.persistence.NameDateInterval;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.TemporaryValue;
import org.flexpay.common.persistence.Translation;

import java.util.Map;

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
		log.info("Object: " + object);
		if (object.getId() == null) {
			Map session = ActionContext.getContext().getSession();
			NTD reg = (NTD) session.remove(ATTRIBUTE_OBJECT);
			if (reg != null) {
				object = reg;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String doExecute() {

		log.info("Object: " + object);
		if (object.getId() != null) {
			object = nameTimeDependentService.read(object.getId());
			return SUCCESS;
		} else {
			addActionError(getText("error.no_id"));
			return ERROR;
		}
	}

	/**
	 * Getter for property 'object'.
	 *
	 * @return Value for property 'object'.
	 */
	public NTD getObject() {
		return object;
	}

	/**
	 * Setter for property 'object'.
	 *
	 * @param object Value to set for property 'object'.
	 */
	public void setObject(NTD object) {
		this.object = object;
	}

	/**
	 * Get initial set of filters for action
	 *
	 * @return Collection of filters
	 */
	protected ArrayStack getFilters() {
		return null;
	}

	/**
	 * Set filters for action
	 *
	 * @param filters collection of filters
	 */
	protected void setFilters(ArrayStack filters) {
	}
}
