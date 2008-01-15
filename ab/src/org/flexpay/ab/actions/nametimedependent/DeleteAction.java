package org.flexpay.ab.actions.nametimedependent;

import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.ab.actions.region.RegionsList;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.NameDateInterval;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.TemporaryValue;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;

import java.util.*;

public abstract class DeleteAction<
		TV extends TemporaryValue<TV>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>,
		T extends Translation> extends ActionBase<TV, DI, NTD, T> implements SessionAware {

	private Map<String, Object> session;

	/**
	 * {@inheritDoc}
	 */
	public String execute() {

		try {
			Collection<PrimaryKeyFilter> filters = parentService.initFilters(getFilters(), userPreferences.getLocale());
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
				addActionError(getText("error.no_regions_to_disable"));
			}
		} catch (FlexPayException e) {
			addActionError(e);
		} catch (FlexPayExceptionContainer container) {
			addActionErrors(container);
		}

		// Save action errors for forwarded action
		if (!getActionErrors().isEmpty()) {
			RegionsList.setActionErrors(session, getActionErrors());
		}

		return SUCCESS;
	}

	/**
	 * Sets the Map of session attributes in the implementing class.
	 *
	 * @param session a Map of HTTP session attribute name/value pairs.
	 */
	@SuppressWarnings ("unchecked")
	public void setSession(Map session) {
		this.session = session;
	}

	private Set<Long> objectIds = new HashSet<Long>();

	/**
	 * Getter for property 'townTypeIds'.
	 *
	 * @return Value for property 'townTypeIds'.
	 */
	public Set<Long> getObjectIds() {
		return objectIds;
	}

	/**
	 * Setter for property 'townTypeIds'.
	 *
	 * @param objectIds Value to set for property 'townTypeIds'.
	 */
	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

}
