package org.flexpay.ab.service;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.IdentityTypeTranslation;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

public interface IdentityTypeService extends
		MultilangEntityService<IdentityType, IdentityTypeTranslation> {

	/**
	 * Find identity type by enum id
	 *
	 * @param typeId Type id
	 * @return IdentityType if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.IDENTITY_TYPE_READ)
	IdentityType getType(int typeId);

	/**
	 * Find identity type by name
	 *
	 * @param typeName Type name
	 * @return IdentityType if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.IDENTITY_TYPE_READ)
	IdentityType getType(String typeName);

	/**
	 * Read Entity object by its unique id
	 *
	 * @param id Entity key
	 * @return Entity object, or <code>null</code> if object not found
	 */
	@Secured (Roles.IDENTITY_TYPE_READ)
	IdentityType read(Long id);

	/**
	 * Get Entity translations for specified locale, if translation is not found check for translation in default locale
	 *
	 * @param locale Locale to get translations for
	 * @return List of Translation
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	@Secured (Roles.IDENTITY_TYPE_READ)
	List<IdentityTypeTranslation> getTranslations(Locale locale) throws FlexPayException;

	/**
	 * Get a list of available street types
	 *
	 * @return List of Entity
	 */
	@Secured (Roles.IDENTITY_TYPE_READ)
	@NotNull
	List<IdentityType> getEntities();

	/**
	 * Disable Entity
	 *
	 * @param entity Entity to disable
	 */
	@Secured (Roles.IDENTITY_TYPE_DELETE)
	void disable(Collection<IdentityType> entity);

	/**
	 * Update or create Entity
	 *
	 * @param entity Entity to save
	 * @return Saved instance
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured ({Roles.IDENTITY_TYPE_ADD, Roles.IDENTITY_TYPE_CHANGE})
	IdentityType save(@NotNull IdentityType entity) throws FlexPayExceptionContainer;
}
