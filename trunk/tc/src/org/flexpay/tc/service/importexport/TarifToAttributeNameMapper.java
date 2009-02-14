package org.flexpay.tc.service.importexport;

import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.Map;

public class TarifToAttributeNameMapper {

	private Map<String, String> attributeName2tarifCodes = Collections.emptyMap();

	public String getTarifCodeByAttributeName(String tarifCode) {

		return attributeName2tarifCodes.get(tarifCode);
	}

	@Required
	public void setAttributeName2tarifCodes(Map<String, String> attributeName2tarifCodes) {
		this.attributeName2tarifCodes = attributeName2tarifCodes;
	}
}
