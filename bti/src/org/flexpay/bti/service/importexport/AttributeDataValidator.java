package org.flexpay.bti.service.importexport;

import org.flexpay.common.exception.FlexPayException;

public interface AttributeDataValidator {

	void validate(BuildingAttributeData data) throws FlexPayException;
}
