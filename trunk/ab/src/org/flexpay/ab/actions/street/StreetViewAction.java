package org.flexpay.ab.actions.street;

import org.flexpay.ab.actions.nametimedependent.ObjectViewAction;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.StreetService;
import static org.flexpay.common.persistence.Stub.stub;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Collections;

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
