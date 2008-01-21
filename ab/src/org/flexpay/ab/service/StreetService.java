package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetName;
import org.flexpay.ab.persistence.StreetNameTemporal;
import org.flexpay.ab.persistence.StreetNameTranslation;
import org.flexpay.common.service.NameTimeDependentService;

public interface StreetService extends NameTimeDependentService<
		StreetName, StreetNameTemporal, Street, StreetNameTranslation> {
}
