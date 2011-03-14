package org.flexpay.tc.action.buildingattributes;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.util.ValidationUtil;
import org.flexpay.tc.util.config.ApplicationConfig;

import java.util.Calendar;

/**
 * Provides a set of validation methods for building attributes
 * <p/>
 * Each method returns a <code>null</code> if attribute is OK, and error message i18n code otherwise
 * <p/>
 * WARNING: this object is not thread-safe!
 */
public class BuildingAttributesValidator {

	private String errorMessageCode;

	/**
	 * Checks whether given number is an integer and has value not bigger than maximal allowed number of floors
	 *
	 * @param value value to be checked
	 * @return <code>true</code> if value is OK, <code>false</code> otherwise
	 * @see org.flexpay.tc.util.config.ApplicationConfig
	 */
	public boolean checkFloorsNumber(String value) {

		errorMessageCode = null;

		if (StringUtils.isEmpty(value)) {
			return true; // empty value is allowed
		}

		try {
			Integer floorsNumber = Integer.parseInt(value);

			if (floorsNumber > ApplicationConfig.getMaximumFloors()) {
				errorMessageCode = "tc.errors.building_attributes.validation.number_of_floors_is_too_big";
				return false;
			}

		} catch (NumberFormatException nfe) {
			errorMessageCode = "tc.errors.building_attributes.validation.number_of_floors_is_not_a_number";
			return false;
		}

		return true;
	}

	/**
	 * Checks whether given number is an integer and has value not bigger than maximal allowed number of
	 * porches(doorways)
	 *
	 * @param value value to be checked
	 * @return <code>true</code> if value is OK, <code>false</code> otherwise
	 * @see org.flexpay.tc.util.config.ApplicationConfig
	 */
	public boolean checkPorchesNumber(String value) {

		errorMessageCode = null;

		if (StringUtils.isEmpty(value)) {
			return true; // empty value is allowed
		}

		try {
			Integer porchesNumber = Integer.parseInt(value);

			if (porchesNumber > ApplicationConfig.getMaximumPporches()) {
				errorMessageCode = "tc.errors.building_attributes.validation.number_of_porches_is_too_big";
				return false;
			}
		} catch (NumberFormatException nfe) {
			errorMessageCode = "tc.errors.building_attributes.validation.number_of_porches_is_not_a_number";
			return false;
		}

		return true;
	}

	/**
	 * Checks whether given number is an integer and has value not bigger than maximal allowed number of apartments
	 *
	 * @param value value to be checked
	 * @return <code>true</code> if value is OK, <code>false</code> otherwise
	 * @see org.flexpay.tc.util.config.ApplicationConfig
	 */
	public boolean checkApartmentsNumber(String value) {

		errorMessageCode = null;

		if (StringUtils.isEmpty(value)) {
			return true; // empty value is allowed
		}

		try {
			Integer apartmentsNumber = Integer.parseInt(value);

			if (apartmentsNumber > ApplicationConfig.getMaximumApartments()) {
				errorMessageCode = "tc.errors.building_attributes.validation.number_of_apartments_is_too_big";
				return false;
			}
		} catch (NumberFormatException nfe) {
			errorMessageCode = "tc.errors.building_attributes.validation.number_of_apartments_is_not_a_number";
			return false;
		}

		return true;
	}

	/**
	 * Checks whether given value is a proper year in the past
	 *
	 * @param value value to be checked
	 * @return <code>true</code> if value is OK, <code>false</code> otherwise
	 */
	public boolean checkConstructionYear(String value) {

		errorMessageCode = null;

		if (StringUtils.isEmpty(value)) {
			return true; // empty value is allowed
		}

		if (value.length() != 4) {
			errorMessageCode = "tc.errors.building_attributes.validation.invalid_construction_year";
			return false;
		}

		try {
			int constructionYear = Integer.parseInt(value);
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);

			if (constructionYear > currentYear) {
				errorMessageCode = "tc.errors.building_attributes.validation.invalid_construction_year";
				return false;
			}
		} catch (NumberFormatException nfe) {
			errorMessageCode = "tc.errors.building_attributes.validation.invalid_construction_year";
			return false;
		}

		return true;
	}

	/**
	 * Checks whether given value is a positive decimal number with no more than two digits after decimal point
	 * <p/>
	 * WARNING: no particular message code is set after check
	 *
	 * @param value value to be checked
	 * @return <code>true</code> if value is OK, <code>false</code> otherwise
	 */
	public boolean checkArea(String value) {

		errorMessageCode = null;

		return StringUtils.isEmpty(value) || ValidationUtil.checkAreaValue(value);
	}


	public String getErrorMessageCode() {
		return errorMessageCode;
	}

}
