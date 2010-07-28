package org.flexpay.eirc.service.importexport;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.service.importexport.RawData;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.Registry;

import java.util.Collection;
import java.util.HashSet;

public class RawConsumerData extends RawData<Consumer> {

	private static final Collection<String> possibleNames = new HashSet<String>();

	public static final String FIELD_FIRST_NAME = "firstName";
	public static final String FIELD_MIDDLE_NAME = "middleName";
	public static final String FIELD_LAST_NAME = "lastName";
	public static final String FIELD_ACCOUNT_NUMBER = "accountNumber";
	public static final String FIELD_SERVICE_CODE = "serviceCode";
    public static final String FIELD_ADDRESS_TOWN_TYPE = "townType";
	public static final String FIELD_ADDRESS_TOWN = "town";
    public static final String FIELD_ADDRESS_STREET_TYPE = "streetType";
	public static final String FIELD_ADDRESS_STREET = "street";
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
		possibleNames.add(FIELD_SERVICE_CODE);
        possibleNames.add(FIELD_ADDRESS_TOWN_TYPE);
		possibleNames.add(FIELD_ADDRESS_TOWN);
        possibleNames.add(FIELD_ADDRESS_STREET_TYPE);
		possibleNames.add(FIELD_ADDRESS_STREET);
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

	public String getServiceCode() {
		return getParam(FIELD_SERVICE_CODE);
	}

    public String getAddressTownType() {
        return getParam(FIELD_ADDRESS_TOWN_TYPE);
    }

	public String getAddressTown() {
		return getParam(FIELD_ADDRESS_TOWN);
	}

    public String getAddressStreetType() {
        return getParam(FIELD_ADDRESS_STREET_TYPE);
    }

	public String getAddressStreet() {
		return getParam(FIELD_ADDRESS_STREET);
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

	public RegistryRecord getRegistryRecord() {
		Object obj = getNameToValuesMap().get(FIELD_REGISTRY_RECORD);
		return obj == null ? null : (RegistryRecord) obj;
	}

	public Registry getRegistry() {
		Object obj = getNameToValuesMap().get(FIELD_REGISTRY_HEADER);
		return obj == null ? null : (Registry) obj;
	}

	public String getPersonFIO() {
		return new StringBuilder()
				.append(getFirstName()).append('|')
				.append(getMiddleName()).append('|')
				.append(getLastName()).append('|')
				.toString();
	}

	/**
	 * Get correction id for apartment, includes BuioldingId | Apartment
	 *
	 * @return Correction ID for apartment
	 */
	public String getApartmentId() {
		return new StringBuilder()
				.append(getBuildingId()).append('|')
				.append(getAddressApartment())
				.toString();
	}

	/**
	 * Get correction id for building, includes StreetId | House | Bulk
	 *
	 * @return Correction ID for building
	 */
	public String getBuildingId() {
		return new StringBuilder()
				.append(getStreetId()).append('|')
				.append(getAddressHouse()).append('|')
				.append(getAddressBulk())
				.toString();
	}

	/**
	 * Get correction id for street, includes Town | Street Name | Street Type
	 *
	 * @return Correction ID for street
	 */
	public String getStreetId() {
		return new StringBuilder()
				.append(getAddressTown()).append('|')
				.append(getAddressStreet()).append('|')
				.append(getAddressStreetType())
				.toString();
	}

    /**
     * Get correction id for street type
     *
     * @return Correction ID for street
     */
    public String getStreetTypeId() {
        return new StringBuilder()
                .append(getAddressStreetType())
                .toString();
    }

	/**
	 * Get correction id for consumer, includes Account number | service code
	 *
	 * @return Correction ID for consumer
	 */
	public String getShortConsumerId() {
		return new StringBuilder()
				.append(getAccountNumber()).append('|')
				.append(getServiceCode())
				.toString();
	}

	/**
	 * Get correction id for consumer, includes Account number | service code
	 *
	 * @return Correction ID for street
	 */
	public String getFullConsumerId() {
		return new StringBuilder()
				.append(getApartmentId()).append('|')
				.append(getPersonFIO()).append('|')
				.append(getAccountNumber()).append('|')
				.append(getServiceCode())
				.toString();
	}

	/**
	 * Check if personal information is empty, i.e. first-last-maddle names and address information was not specified
	 * To find a consumer it is necessary to use short consimer id ({@link #getShortConsumerId()})
	 *
	 * @return <code
	 */
	public boolean isPersonalInfoEmpty() {
		return StringUtils.isBlank(getAddressStreet())
				&& StringUtils.isBlank(getAddressBulk())
				&& StringUtils.isBlank(getAddressHouse())
				&& StringUtils.isBlank(getAddressApartment())
				&& StringUtils.isBlank(getFirstName())
				&& StringUtils.isBlank(getLastName())
				&& StringUtils.isBlank(getMiddleName())
				&& StringUtils.isBlank(getAddressStreetType());
	}

	/**
	 * Get correction id for person, includes Account number | Address | FIO
	 *
	 * @return Correction ID for person
	 */
	public String getPersonFIOId() {
		return new StringBuilder()
				.append(getPersonFIO())
				.append(getAccountNumber()).append("|")
				.append(getApartmentId())
				.toString();
	}
}
