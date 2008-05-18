package org.flexpay.eirc.dao.importexport;

import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.common.service.importexport.RawDataSource;
import org.apache.log4j.Logger;

import java.util.Iterator;

public abstract class RawConsumersDataSourceBase implements RawDataSource<RawConsumerData> {

	protected Logger log = Logger.getLogger(getClass());

	protected Iterator<SpRegistryRecord> dataIterator;

	/**
	 * Check if source is trusted and new objects are allowed to be created from this source
	 *
	 * @return <code>true</code> if the source is trusted, or <code>false</code> otherwise
	 */
	public boolean trusted() {
		return false;
	}

	protected RawConsumerData convert(SpRegistryRecord record) {
		if (record == null) {
			return null;
		}

		RawConsumerData data = new RawConsumerData();
		data.setExternalSourceId(String.valueOf(record.getId()));
		data.addNameValuePair(RawConsumerData.FIELD_ACCOUNT_NUMBER, record.getPersonalAccountExt());
		data.addNameValuePair(RawConsumerData.FIELD_FIRST_NAME, record.getFirstName());
		data.addNameValuePair(RawConsumerData.FIELD_MIDDLE_NAME, record.getMiddleName());
		data.addNameValuePair(RawConsumerData.FIELD_LAST_NAME, record.getLastName());
		data.addNameValuePair(RawConsumerData.FIELD_ADDRESS_CITY, record.getCity());
		data.addNameValuePair(RawConsumerData.FIELD_ADDRESS_STREET, record.getStreetName());
		data.addNameValuePair(RawConsumerData.FIELD_ADDRESS_STREET_TYPE, record.getStreetType());
		data.addNameValuePair(RawConsumerData.FIELD_ADDRESS_HOUSE, record.getBuildingNum());
		data.addNameValuePair(RawConsumerData.FIELD_ADDRESS_BULK, record.getBuildingBulkNum());
		data.addNameValuePair(RawConsumerData.FIELD_ADDRESS_APARTMENT, record.getApartmentNum());

		data.addNameValuePair(RawConsumerData.FIELD_REGISTRY_HEADER, record.getSpRegistry());
		data.addNameValuePair(RawConsumerData.FIELD_REGISTRY_RECORD, record);

		return data;
	}

	/**
	 * Returns the next new imported element in the iteration.
	 *
	 * @param holder Operation type holder
	 * @return the next element in the iteration.
	 * @throws java.util.NoSuchElementException
	 *          iteration has no more elements.
	 */
	public RawConsumerData next(ImportOperationTypeHolder holder) {
		return convert(dataIterator.next());
	}
}
