package org.flexpay.ab.actions.nametimedependent;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.NameDateInterval;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.TemporaryValue;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.service.NameTimeDependentService;
import org.flexpay.common.service.ParentService;

public abstract class ActionBase<
		TV extends TemporaryValue<TV>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>,
		T extends Translation> extends FPActionSupport {

	protected NameTimeDependentService<TV, DI, NTD, T> nameTimeDependentService;
	protected ParentService parentService;

	/**
	 * Setter for property 'service'.
	 *
	 * @param nameTimeDependentService Value to set for property 'service'.
	 */
	public void setNameTimeDependentService(NameTimeDependentService<TV, DI, NTD, T> nameTimeDependentService) {
		this.nameTimeDependentService = nameTimeDependentService;
	}

	/**
	 * Setter for property 'parentService'.
	 *
	 * @param parentService Value to set for property 'parentService'.
	 */
	public void setParentService(ParentService parentService) {
		this.parentService = parentService;
	}

	/**
	 * Get initial set of filters for action
	 *
	 * @return Collection of filters
	 */
	protected abstract ArrayStack getFilters();

	/**
	 * Set filters for action
	 *
	 * @param filters collection of filters
	 */
	protected abstract void setFilters(ArrayStack filters);
}
