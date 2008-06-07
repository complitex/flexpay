package org.flexpay.eirc.dao.importexport;

import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.SpRegistry;

public class RawConsumersDataUtil {

	public static RawConsumerData convert(SpRegistryRecord record) {
		if (record == null) {
			return null;
		}

		return convert(record.getSpRegistry(), record);
	}

	public static RawConsumerData convert(SpRegistry registry, SpRegistryRecord record) {
		if (record == null) {
			return null;
		}

		RawConsumerData data = new RawConsumerData();
		data.setExternalSourceId(String.valueOf(record.getId()));
		data.addNameValuePair(RawConsumerData.FIELD_ACCOUNT_NUMBER, record.getPersonalAccountExt());
		data.addNameValuePair(RawConsumerData.FIELD_SERVICE_CODE, record.getServiceCode());
		data.addNameValuePair(RawConsumerData.FIELD_FIRST_NAME, record.getFirstName());
		data.addNameValuePair(RawConsumerData.FIELD_MIDDLE_NAME, record.getMiddleName());
		data.addNameValuePair(RawConsumerData.FIELD_LAST_NAME, record.getLastName());
		data.addNameValuePair(RawConsumerData.FIELD_ADDRESS_CITY, record.getCity());
		data.addNameValuePair(RawConsumerData.FIELD_ADDRESS_STREET, record.getStreetName());
		data.addNameValuePair(RawConsumerData.FIELD_ADDRESS_STREET_TYPE, record.getStreetType());
		data.addNameValuePair(RawConsumerData.FIELD_ADDRESS_HOUSE, record.getBuildingNum());
		data.addNameValuePair(RawConsumerData.FIELD_ADDRESS_BULK, record.getBuildingBulkNum());
		data.addNameValuePair(RawConsumerData.FIELD_ADDRESS_APARTMENT, record.getApartmentNum());

		data.addNameValuePair(RawConsumerData.FIELD_REGISTRY_HEADER, registry);
		data.addNameValuePair(RawConsumerData.FIELD_REGISTRY_RECORD, record);

		return data;
	}
}
