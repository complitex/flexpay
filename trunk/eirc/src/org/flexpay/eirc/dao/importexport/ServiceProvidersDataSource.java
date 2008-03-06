package org.flexpay.eirc.dao.importexport;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.eirc.dao.SpRegistryRecordDao;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.service.importexport.RawPersonalAccountData;

import java.util.Iterator;
import java.util.List;

public class ServiceProvidersDataSource implements RawDataSource<RawPersonalAccountData> {

	private SpRegistryRecordDao registryRecordDao;

	private Page<SpRegistryRecord> pager;
	private Iterator<SpRegistryRecord> dataIterator;

	private SpRegistry registry;

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
	public RawPersonalAccountData getById(String objId) {
		return convert(registryRecordDao.read(Long.valueOf(objId)));
	}

	/**
	 * Initialize data source
	 */
	public void initialize() {
		pager = new Page<SpRegistryRecord>(1000, 1);
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
	public RawPersonalAccountData next(ImportOperationTypeHolder holder) {
		return convert(dataIterator.next());
	}

	private RawPersonalAccountData convert(SpRegistryRecord record) {
		RawPersonalAccountData data = new RawPersonalAccountData();
		data.setExternalSourceId(String.valueOf(record.getId()));
//		data.addNameValuePair(RawPersonalAccountData.FIELD_DISTRICT, record.getDistrict());
//		data.addNameValuePair(RawPersonalAccountData.FIELD_DISTRICT_ID, record.getDistrictId());
		data.addNameValuePair(RawPersonalAccountData.FIELD_STREET, record.getStreetName());
//		data.addNameValuePair(RawPersonalAccountData.FIELD_STREET_ID, record.getStreetId());
		data.addNameValuePair(RawPersonalAccountData.FIELD_STREET_TYPE, record.getStreetType());
		data.addNameValuePair(RawPersonalAccountData.FIELD_BUILDING, record.getBuildingNum());
		data.addNameValuePair(RawPersonalAccountData.FIELD_BULK, record.getBuildingBulkNum());
//		data.addNameValuePair(RawPersonalAccountData.FIELD_BUILDING_ID, record.getBuildingId());
		data.addNameValuePair(RawPersonalAccountData.FIELD_APARTMENT, record.getApartmentNum());
//		data.addNameValuePair(RawPersonalAccountData.FIELD_APARTMENT_ID, record.getApartmentId());
		data.addNameValuePair(RawPersonalAccountData.FIELD_EXT_ACCOUNT, record.getPersonalAccountExt());
		data.addNameValuePair(RawPersonalAccountData.FIELD_FIRST_NAME, record.getFirstName());
//		data.addNameValuePair(RawPersonalAccountData.FIELD_MIDDLE_NAME, record.getMiddleName());
		data.addNameValuePair(RawPersonalAccountData.FIELD_LAST_NAME, record.getLastName());
		return data;
	}

	/**
	 * Setter for property 'registryRecordDao'.
	 *
	 * @param registryRecordDao Value to set for property 'registryRecordDao'.
	 */
	public void setRegistryRecordDao(SpRegistryRecordDao registryRecordDao) {
		this.registryRecordDao = registryRecordDao;
	}

	/**
	 * Setter for property 'registry'.
	 *
	 * @param registry Value to set for property 'registry'.
	 */
	public void setRegistry(SpRegistry registry) {
		this.registry = registry;
	}
}
