package org.flexpay.eirc.service.importexport;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.service.importexport.RawData;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.SpRegistry;

import java.util.Collection;
import java.util.HashSet;

public class RawConsumerData extends RawData<Consumer> {

	private static Collection<String> possibleNames = new HashSet<String>();

	public static final String FIELD_FIRST_NAME = "firstName";
	public static final String FIELD_MIDDLE_NAME = "middleName";
	public static final String FIELD_LAST_NAME = "lastName";
	public static final String FIELD_ACCOUNT_NUMBER = "accountNumber";
	public static final String FIELD_ADDRESS_CITY = "city";
	public static final String FIELD_ADDRESS_STREET = "street";
	public static final String FIELD_ADDRESS_STREET_TYPE = "streetType";
	public static final String FIELD_ADDRESS_HOUSE = "house";
	public static final String FIELD_ADDRESS_BULK = "bulk";
	public static final String FIELD_ADDRESS_APARTMENT = "apartment";
	public static final String FIELD_REGISTRY_RECORD = "record";
	public static final String FIELD_REGISTRY_HEADER = "header";

	static {
		possibleNames.add("id");
		possibleNames.add(FIELD_FIRST_NAME);
		possibleNames.add(FIELD_MIDDLE_NAME);
		possibleNames.add(FIELD_LAST_NAME);
		possibleNames.add(FIELD_ACCOUNT_NUMBER);
		possibleNames.add(FIELD_ADDRESS_CITY);
		possibleNames.add(FIELD_ADDRESS_STREET);
		possibleNames.add(FIELD_ADDRESS_STREET_TYPE);
		possibleNames.add(FIELD_ADDRESS_HOUSE);
		possibleNames.add(FIELD_ADDRESS_BULK);
		possibleNames.add(FIELD_ADDRESS_APARTMENT);
	}

	/**
	 * Get set of valid attribute names
	 *
	 * @return Set of attribute names;
	 */
	public Collection<String> getPossibleNames() {
		return possibleNames;
	}

	public String getFirstName() {
		return getParam(FIELD_FIRST_NAME);
	}

	public String getMiddleName() {
		return getParam(FIELD_MIDDLE_NAME);
	}

	public String getLastName() {
		return getParam(FIELD_LAST_NAME);
	}

	public String getAccountNumber() {
		return getParam(FIELD_ACCOUNT_NUMBER);
	}

	public String getAddressCity() {
		return getParam(FIELD_ADDRESS_CITY);
	}

	public String getAddressStreet() {
		return getParam(FIELD_ADDRESS_STREET);
	}

	public String getAddressStreetType() {
		return getParam(FIELD_ADDRESS_STREET_TYPE);
	}

	public String getAddressHouse() {
		return getParam(FIELD_ADDRESS_HOUSE);
	}

	public String getAddressBulk() {
		return getParam(FIELD_ADDRESS_BULK);
	}

	public String getAddressApartment() {
		return getParam(FIELD_ADDRESS_APARTMENT);
	}

	public SpRegistryRecord getRegistryRecord() {
		Object obj = getNameToValuesMap().get(FIELD_REGISTRY_RECORD);
		return obj == null ? null : (SpRegistryRecord) obj;
	}

	public SpRegistry getRegistry() {
		Object obj = getNameToValuesMap().get(FIELD_REGISTRY_HEADER);
		return obj == null ? null : (SpRegistry) obj;
	}

	public String getCorrectionId() {
		String[] parts = {
				getFirstName(), getMiddleName(), getLastName(), getAccountNumber(),
				getAddressCity(), getAddressStreet(), getAddressStreetType(),
				getAddressHouse(), getAddressBulk(), getAddressApartment()
		};
		return StringUtils.join(parts, '|');
	}

	public String getPersonCorrectionId() {
		String[] parts = {
				getFirstName(), getMiddleName(), getLastName(), getAccountNumber(),
		};
		return StringUtils.join(parts, '|');
	}

	public String getApartmentId() {
		return new StringBuilder()
				.append(getBuildingId())
				.append(getAddressApartment()).append('|')
				.toString();
	}

	public String getBuildingId() {
		return new StringBuilder()
				.append(getStreetId())
				.append(getAddressHouse()).append('|')
				.append(getAddressBulk()).append('|')
				.toString();
	}

	public String getStreetId() {
		return new StringBuilder()
				.append(getAddressCity()).append('|')
				.append(getAddressStreet()).append('|')
				.append(getAddressStreetType()).append('|')
				.toString();
	}
}
