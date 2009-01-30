package org.flexpay.bti.service.importexport.impl;

import org.flexpay.bti.service.importexport.AttributeNameMapper;

public class CNCSVAttributeNameMapper implements AttributeNameMapper {

	/**
	 * Get name of the n-th attribute, returned <code>null</code> is for unknown position of attribute
	 *
	 * @param n Order of attribute to get name for
	 * @return Attribute name
	 */
	public String getName(int n) {
		// todo implement me using config
		return null;
	}
}
