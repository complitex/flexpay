package org.flexpay.ab.actions.nametimedependent;

import org.apache.commons.collections.ArrayStack;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.common.persistence.NameDateInterval;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.TemporaryValue;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class DeleteAction<
		TV extends TemporaryValue<TV>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>,
		T extends Translation> extends ActionBase<TV, DI, NTD, T> implements SessionAware {

	private Set<Long> objectIds = new HashSet<Long>();

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public String doExecute() throws Exception {

		ArrayStack filters = parentService.initFilters(getFilters(), userPreferences.getLocale());
		setFilters(filters);

		Collection<NTD> objectToDisable = new ArrayList<NTD>();
		for (NTD object : nameTimeDependentService.find(filters)) {
			if (objectIds.contains(object.getId())) {
				objectToDisable.add(object);
			}
		}

		if (!objectToDisable.isEmpty()) {
			nameTimeDependentService.disable(objectToDisable);
		} else {
			addActionError(getText("error.no_objects_to_disable"));
		}

		return REDIRECT_SUCCESS;
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
		return REDIRECT_SUCCESS;
	}

	@Override
	protected void setBreadCrumbs() {
		
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

}