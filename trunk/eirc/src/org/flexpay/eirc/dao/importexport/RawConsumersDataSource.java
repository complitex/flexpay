package org.flexpay.eirc.dao.importexport;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.eirc.dao.SpRegistryRecordDao;
import org.flexpay.eirc.dao.SpRegistryRecordDaoExt;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.service.importexport.RawConsumerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RawConsumersDataSource extends RawConsumersDataSourceBase {

	private SpRegistryRecordDao registryRecordDao;
	private SpRegistryRecordDaoExt registryRecordDaoExt;
	private SpRegistry registry;

	private Page<SpRegistryRecord> pager;

	/**
	 * Find raw data by its id
	 *
	 * @param objId Raw data id
	 * @return raw data
	 */
	public RawConsumerData getById(String objId) {
		return convert(registryRecordDao.read(Long.parseLong(objId)));
	}

	/**
	 * Initialize data source
	 */
	public void initialize() {
		pager = new Page<SpRegistryRecord>(5000, 1);
		List<SpRegistryRecord> datum = registryRecordDaoExt.listRecordsForUpdate(registry.getId(), pager);
		dataIterator = datum.iterator();

		log.debug("Inited db data source");
	}

	/**
	 * Release all resources taken
	 */
	public void close() {
	}

	/**
	 * Returns <tt>true</tt> if the iteration has more elements. (In other words, returns <tt>true</tt> if <tt>next</tt> would return
	 * an element rather than throwing an exception.)
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
		List<SpRegistryRecord> datum = registryRecordDaoExt.listRecordsForUpdate(registry.getId(), pager);
		dataIterator = datum.iterator();
		return dataIterator.hasNext();
	}

	public List<RawConsumerData> nextPage() {

		if (!dataIterator.hasNext() && !hasNext()) {
			return Collections.emptyList();
		}

		List<RawConsumerData> datum = new ArrayList<RawConsumerData>();
		while (dataIterator.hasNext()) {
			datum.add(next(new ImportOperationTypeHolder()));
		}

		return datum;
	}

	/**
	 * Setter for property 'registry'.
	 *
	 * @param registry Value to set for property 'registry'.
	 */
	public void setRegistry(SpRegistry registry) {
		this.registry = registry;
	}

	protected RawConsumerData convert(SpRegistryRecord record) {
		record.setSpRegistry(registry);
		return RawConsumersDataUtil.convert(record);
	}

	public void setRegistryRecordDao(SpRegistryRecordDao registryRecordDao) {
		this.registryRecordDao = registryRecordDao;
	}

	public void setRegistryRecordDaoExt(SpRegistryRecordDaoExt registryRecordDaoExt) {
		this.registryRecordDaoExt = registryRecordDaoExt;
	}
}
