package org.flexpay.ab.service;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.IdentityTypeTranslation;

import java.util.Collection;

public interface IdentityTypeService extends
		MultilangEntityService<IdentityType, IdentityTypeTranslation> {

	/**
	 * Find identity type by enum id
	 *
	 * @param typeId Type id
	 * @return IdentityType if found, or <code>null</code> otherwise
	 */
	IdentityType getType(int typeId);

	/**
	 * Find identity type by name
	 *
	 * @param typeName Type name
	 * @return IdentityType if found, or <code>null</code> otherwise
	 */
	IdentityType getType(String typeName);

	/**
	 * Get available identity types
	 *
	 * @return Identity types
	 */
	Collection<IdentityType> getIdentityTypes();
}
