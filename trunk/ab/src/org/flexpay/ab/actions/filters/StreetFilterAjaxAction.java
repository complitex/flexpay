package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

/**
 * Search streets by name
 */
public class StreetFilterAjaxAction extends FilterAjaxAction {

	private StreetService streetService;

	@NotNull
	public String doExecute() throws FlexPayException {

		if (saveFilterValue()) {
			return SUCCESS;
		}

		Long townIdLong;

		try {
			townIdLong = Long.parseLong(parents[0]);
		} catch (Exception e) {
			log.warn("Incorrect town id in filter ({})", parents[0]);
			return SUCCESS;
		}

		List<Street> streets = streetService.findByTownAndQuery(new Stub<Town>(townIdLong), "%" + q + "%");
		log.debug("Found streets: {}", streets);

		foundObjects = new ArrayList<FilterObject>();
		for (Street street : streets) {
			FilterObject object = new FilterObject();
			object.setValue(street.getId() + "");
			object.setName(getTranslation(street.getCurrentType().getTranslations()).getName()
							  + " " + getTranslation(street.getCurrentName().getTranslations()).getName());
			foundObjects.add(object);
		}

		return SUCCESS;
	}

	public boolean saveFilterValue() {
		if (filterValue != null) {
			try {
				UserPreferences prefs = UserPreferences.getPreferences(request);
				prefs.setStreetFilterValue(Long.parseLong(filterValue));
				UserPreferences.setPreferences(request, prefs);
				return true;
			} catch (Exception e) {
				log.warn("Incorrect country id in filter ({})", filterValue);
			}
		}
		return false;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

}
