package org.flexpay.eirc.dao.importexport;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.eirc.dao.SpRegistryRecordDao;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.service.importexport.RawConsumerData;

import java.util.Iterator;
import java.util.List;

public class RawConsumersDataSource implements RawDataSource<RawConsumerData> {

	private SpRegistryRecordDao registryRecordDao;
	private SpRegistry registry;

	private Page<SpRegistryRecord> pager;
	private Iterator<SpRegistryRecord> dataIterator;

	/**
	 * Check if source is trusted and new objects are allowed to be created from this source
	 *
	 * @return <code>true</code> if the source is trusted, or <code>false</code> otherwise
	 */
	public boolean trusted() {
		return false;
	}

	/**
	 * Find raw data by its id
	 *
	 * @param objId Raw data id
	 * @return raw data
	 */
	public RawConsumerData getById(String objId) {
		return convert(registryRecordDao.read(Long.parseLong(objId)));
	}

	private RawConsumerData convert(SpRegistryRecord record) {
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

		data.addNameValuePair(RawConsumerData.FIELD_REGISTRY_HEADER, registry);
		data.addNameValuePair(RawConsumerData.FIELD_REGISTRY_RECORD, record);

		return data;
	}

	/**
	 * Initialize data source
	 */
	public void initialize() {
		pager = new Page<SpRegistryRecord>(10000, 1);
		List<SpRegistryRecord> datum = registryRecordDao.listRecords(registry.getId(), pager);
		dataIterator = datum.iterator();
	}

	/**
	 * Returns <tt>true</tt> if the iteration has more elements. (In other words, returns
	 * <tt>true</tt> if <tt>next</tt> would return an element rather than throwing an
	 * exception.)
	 *
	 * @return <tt>true</tt> if the iterator has more elements.
	 */
	public boolean hasNext() {
		if (dataIterator.hasNext()) {
			return true;
		}

		// get next page
		int nextPage = pager.getPageNumber() + 1;
		pager.setPageNumber(nextPage);
		List<SpRegistryRecord> datum = registryRecordDao.listRecords(registry.getId(), pager);
		dataIterator = datum.iterator();
		return dataIterator.hasNext();
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

	/**
	 * Setter for property 'registry'.
	 *
	 * @param registry Value to set for property 'registry'.
	 */
	public void setRegistry(SpRegistry registry) {
		this.registry = registry;
	}

	/**
	 * Setter for property 'registryRecordDao'.
	 *
	 * @param registryRecordDao Value to set for property 'registryRecordDao'.
	 */
	public void setRegistryRecordDao(SpRegistryRecordDao registryRecordDao) {
		this.registryRecordDao = registryRecordDao;
	}
}
