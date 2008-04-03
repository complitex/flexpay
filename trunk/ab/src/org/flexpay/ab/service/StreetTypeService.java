package org.flexpay.ab.service;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.common.exception.FlexPayException;

public interface StreetTypeService extends
		MultilangEntityService<StreetType, StreetTypeTranslation> {

	/**
	 * Find street type by name
	 *
	 * @param typeName type name in any language
	 * @return the first matching type, or <code>null</code> if none matches
	 * @throws FlexPayException if failure occurs
	 */
	StreetType findTypeByName(String typeName) throws FlexPayException;
}
