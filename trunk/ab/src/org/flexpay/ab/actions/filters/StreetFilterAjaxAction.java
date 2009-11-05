package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetName;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Search streets by name
 */
public class StreetFilterAjaxAction extends FilterAjaxAction {

	private StreetService streetService;

	@NotNull
	@Override
	public String doExecute() throws FlexPayException {

		Long townId;

		try {
			townId = Long.parseLong(parents[0]);
		} catch (Exception e) {
			log.warn("Incorrect town id in filter ({})", parents[0]);
			return SUCCESS;
		}
		if (townId == 0) {
			return SUCCESS;
		}

		List<Street> streets = streetService.findByParentAndQuery(new Stub<Town>(townId), "%" + q + "%");
		if (log.isDebugEnabled()) {
			log.debug("Found streets: {}", streets.size());
		}

		for (Street street : streets) {
			StreetType type = street.getCurrentType();
			StreetName name = street.getCurrentName();
			if (type == null || name == null) {
				log.warn("Found incorrect street: {}", street);
				continue;
			}
			FilterObject object = new FilterObject();
			object.setValue(street.getId() + "");
			object.setName(getTranslation(type.getTranslations()).getShortName() + " " + getTranslationName(name.getTranslations()));
			foundObjects.add(object);
		}

		return SUCCESS;
	}

	@Override
	public void readFilterString() {
		if (filterValueLong != null && filterValueLong > 0) {
			Street street = streetService.readFull(new Stub<Street>(filterValueLong));
			if (street != null && street.getCurrentName() != null && street.getCurrentType() != null) {
				filterString = getTranslation(street.getCurrentType().getTranslations()).getShortName()
								  + " " + getTranslationName(street.getCurrentName().getTranslations());
			} else {
				filterString = "";
			}
		} else {
			filterString = "";
		}
	}

	@Override
	public void saveFilterValue() {
		getUserPreferences().setStreetFilter(filterValueLong);
		getUserPreferences().setBuildingFilter(0L);
		getUserPreferences().setApartmentFilter(0L);
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

}
