package org.flexpay.tc.service.importexport;

import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.Map;

public class TariffToAttributeNameMapper {

	private Map<String, String> attributeName2tariffCodes = Collections.emptyMap();

	public String getTariffCodeByAttributeName(String tariffCode) {
		return attributeName2tariffCodes.get(tariffCode);
	}

	@Required
	public void setAttributeName2tariffCodes(Map<String, String> attributeName2tariffCodes) {
		this.attributeName2tariffCodes = attributeName2tariffCodes;
	}

}
