package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetName;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.util.config.AbUserPreferences;
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

		if (parents == null) {
			log.warn("Parent parameter is null");
			addActionError(getText("common.error.invalid_id"));
			return SUCCESS;
		}

		Long townId;

		try {
			townId = Long.parseLong(parents[0]);
		} catch (Exception e) {
			log.warn("Incorrect town id in filter ({})", parents[0]);
			addActionError(getText("common.object_not_selected"));
			return SUCCESS;
		}
		if (townId.equals(0L)) {
			return SUCCESS;
		}

		if (q == null) {
			q = "";
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

		filterString = "";

		if (filterValueLong == null || filterValueLong <= 0) {
			return;
		}

		Street street = streetService.readFull(new Stub<Street>(filterValueLong));
		if (street == null) {
			log.warn("Can't get street with id {} from DB", filterValueLong);
			addActionError(getText("common.object_not_selected"));
			return;
		}
		if (street.getCurrentName() != null && street.getCurrentType() != null) {
			filterString = getTranslation(street.getCurrentType().getTranslations()).getShortName()
							  + " " + getTranslationName(street.getCurrentName().getTranslations());
		}
	}

	@Override
	public void saveFilterValue() {
		if (filterString == null) {

			if (filterValueLong == null || filterValueLong <= 0) {
				log.warn("Incorrect filter value {}", filterValue);
				addActionError(getText("common.error.invalid_id"));
				return;
			}

			Street street = streetService.readFull(new Stub<Street>(filterValueLong));
			if (street == null) {
				log.warn("Can't get street with id {} from DB", filterValueLong);
				addActionError(getText("common.object_not_selected"));
				return;
			}
		}

		AbUserPreferences up = getUserPreferences();
		up.setStreetFilter(filterValueLong);
		up.setBuildingFilter(0L);
		up.setApartmentFilter(0L);
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

}
