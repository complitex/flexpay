package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.TownService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class TownFilterAjaxAction extends FilterAjaxAction {

	private TownService townService;

	@NotNull
	public String doExecute() throws FlexPayException {

		if (saveFilterValue()) {
			return SUCCESS;
		}

		Long regionIdLong;

		try {
			regionIdLong = Long.parseLong(parents[0]);
		} catch (Exception e) {
			log.warn("Incorrect region id in filter ({})", parents[0]);
			return SUCCESS;
		}

		List<Town> towns = townService.findByRegionAndQuery(new Stub<Region>(regionIdLong), "%" + q + "%");
		log.debug("Found towns: {}", towns);

		foundObjects = new ArrayList<FilterObject>();
		for (Town town : towns) {
			FilterObject object = new FilterObject();
			object.setValue(town.getId() + "");
			object.setName(getTranslation(town.getCurrentName().getTranslations()).getName());
			foundObjects.add(object);
		}

		return SUCCESS;
	}

	public boolean saveFilterValue() {
		if (filterValue != null) {
			try {
				UserPreferences prefs = UserPreferences.getPreferences(request);
				prefs.setTownFilterValue(Long.parseLong(filterValue));
				UserPreferences.setPreferences(request, prefs);
				return true;
			} catch (Exception e) {
				log.warn("Incorrect country id in filter ({})", filterValue);
			}
		}
		return false;
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

}
