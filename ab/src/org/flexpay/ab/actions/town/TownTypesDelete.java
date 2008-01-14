package org.flexpay.ab.actions.town;

import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;

import java.util.*;

public class TownTypesDelete extends FPActionSupport implements SessionAware {

	private TownTypeService townTypeService;
	private Map<String, Object> session;

	/**
	 * {@inheritDoc}
	 */
	public String execute() {

		Collection<TownType> townTypesToDisable = new ArrayList<TownType>();
		for (TownType townType : townTypeService.getTownTypes()) {
			Long id = townType.getId();
			if (townTypeIds.contains(id)) {
				townTypesToDisable.add(townType);
			}
		}

		if (!townTypesToDisable.isEmpty()) {
			try {
				townTypeService.disable(townTypesToDisable);
			} catch (FlexPayExceptionContainer container) {
				addActionErrors(container);
			}
		} else {
			addActionError(getText("error.no_town_types_to_disable"));
		}

		// Save action errors for forwarded action
		if (!getActionErrors().isEmpty()) {
			TownTypesList.setActionErrors(session, getActionErrors());
		}

		return SUCCESS;
	}

	private Set<Long> townTypeIds = new HashSet<Long>();

	/**
	 * Getter for property 'townTypeIds'.
	 *
	 * @return Value for property 'townTypeIds'.
	 */
	public Set<Long> getTownTypeIds() {
		return townTypeIds;
	}

	/**
	 * Setter for property 'townTypeIds'.
	 *
	 * @param townTypeIds Value to set for property 'townTypeIds'.
	 */
	public void setTownTypeIds(Set<Long> townTypeIds) {
		this.townTypeIds = townTypeIds;
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

	/**
	 * Setter for property 'townTypeService'.
	 *
	 * @param townTypeService Value to set for property 'townTypeService'.
	 */
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}
}
