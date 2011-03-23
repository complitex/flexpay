package org.flexpay.ab.action.street;

import org.flexpay.ab.action.ObjectViewAction;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.StreetService;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

public class StreetViewAction extends ObjectViewAction<
		StreetName, StreetNameTemporal, Street, StreetNameTranslation> {

	private StreetService streetService;

	public StreetViewAction() {
		setObject(new Street());
	}

	public List<District> getDistricts() {
		return object.isNotNew() ? streetService.getStreetDistricts(stub(object)) : Collections.<District>emptyList();
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

}
