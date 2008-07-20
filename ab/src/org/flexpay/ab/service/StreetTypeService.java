package org.flexpay.ab.service;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.persistence.filters.StreetTypeFilter;
import org.flexpay.common.exception.FlexPayException;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public interface StreetTypeService extends
		MultilangEntityService<StreetType, StreetTypeTranslation> {

	/**
	 * Find street type by name
	 *
	 * @param typeName type name in any language
	 * @return the first matching type, or <code>null</code> if none matches
	 * @throws FlexPayException if failure occurs
	 */
	@Nullable
	StreetType findTypeByName(@NotNull String typeName) throws FlexPayException;

	/**
	 * Initialize street type filter
	 *
	 * @param streetTypeFilter Filter to init
	 * @param locale Locale to get filter translations in
	 * @throws FlexPayException if failure occurs
	 */
	void initFilter(StreetTypeFilter streetTypeFilter, Locale locale) throws FlexPayException;
}
