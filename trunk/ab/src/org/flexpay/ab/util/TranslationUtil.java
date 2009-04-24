package org.flexpay.ab.util;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.util.TranslationUtil.getTranslation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Collection;

public class TranslationUtil {

	/**
	 * Find Street name translation in default locale
	 *
	 * @param street Street object
	 * @return Current street name
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
	public static String getNameTranslation(@NotNull Street street) throws FlexPayException {
		return getNameTranslation(street, null);
	}

	/**
	 * Find Street name translation in specified locale
	 *
	 * @param street Street object
	 * @param locale Locale to get translation in
	 * @return Current street name
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
	public static String getNameTranslation(@NotNull Street street, @Nullable Locale locale) throws FlexPayException {
		String streetNameStr = "";
		StreetName name = street.getCurrentName();
		if (name != null) {
			StreetNameTranslation streetNameTranslation = locale == null ?
														  getTranslation(name.getTranslations()) :
														  getTranslation(name.getTranslations(), locale);
			if (streetNameTranslation != null) {
				streetNameStr = streetNameTranslation.getName();
			}
		}
		return streetNameStr;
	}

	/**
	 * Find Street type translation in specified locale
	 *
	 * @param street Street object
	 * @return Current street type
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
	public static String getTypeTranslation(@NotNull Street street) throws FlexPayException {
		return getTypeTranslation(street, null);
	}

	/**
	 * Find Street type translation in specified locale
	 *
	 * @param street Street object
	 * @param locale Locale to get translation in
	 * @return Current street type
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
	public static String getTypeTranslation(@NotNull Street street, @Nullable Locale locale) throws FlexPayException {
		String streetTypeStr = "";
		StreetType type = street.getCurrentType();
		if (type != null) {
			StreetTypeTranslation streetTypeTranslation = locale == null ?
														  getTranslation(type.getTranslations()) :
														  getTranslation(type.getTranslations(), locale);
			if (streetTypeTranslation != null) {
				if (streetTypeTranslation.getShortName() != null) {
					streetTypeStr = streetTypeTranslation.getShortName();
				} else {
					streetTypeStr = streetTypeTranslation.getName();
				}
			}
		}
		return streetTypeStr;
	}

	public static String getBuildingNumber(@Nullable Collection<AddressAttribute> attributes, @Nullable Locale locale) throws FlexPayException {

		if (attributes == null) {
			return null;
		}

		StringBuilder number = new StringBuilder();
		for (AddressAttribute attribute : attributes) {
			if (attribute == null) {
				continue;
			}
			AddressAttributeTypeTranslation attributeTypeTranslation =
					locale == null ? getTranslation(attribute.getBuildingAttributeType().getTranslations()) :
							getTranslation(attribute.getBuildingAttributeType().getTranslations(), locale);
			if (attributeTypeTranslation.getShortName() != null) {
				number.append(attributeTypeTranslation.getShortName()).append(' ');
			} else {
				number.append(attributeTypeTranslation.getName()).append(' ');
			}

			number.append(attribute.getValue()).append(' ');
		}

		return number.toString().trim();
	}

	public static String getBuildingNumberWithoutHouseType(@Nullable Collection<AddressAttribute> attributes, @Nullable Locale locale) throws FlexPayException {

		if (attributes == null) {
			return null;
		}

		StringBuilder number = new StringBuilder();
		for (AddressAttribute attribute : attributes) {
			if (attribute == null) {
				continue;
			}
			if (!ApplicationConfig.getBuildingHouseType().getId().equals(attribute.getBuildingAttributeType().getId())) {
				AddressAttributeTypeTranslation attributeTypeTranslation =
						locale == null ? getTranslation(attribute.getBuildingAttributeType().getTranslations()) :
								getTranslation(attribute.getBuildingAttributeType().getTranslations(), locale);
				if (attributeTypeTranslation.getShortName() != null) {
					number.append(attributeTypeTranslation.getShortName()).append(' ');
				} else {
					number.append(attributeTypeTranslation.getName()).append(' ');
				}
			}
			number.append(attribute.getValue()).append(' ');
		}

		return number.toString().trim();
	}

}
