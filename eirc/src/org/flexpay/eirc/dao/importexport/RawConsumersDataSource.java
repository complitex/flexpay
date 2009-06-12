package org.flexpay.eirc.dao.importexport;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.registry.RegistryRecordDao;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.dao.registry.RegistryRecordDaoExt;
import org.flexpay.eirc.service.importexport.RawConsumerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RawConsumersDataSource extends RawConsumersDataSourceBase {

	private RegistryRecordDao registryRecordDao;
	private RegistryRecordDaoExt registryRecordDaoExt;
	private Registry registry;

	private Page<RegistryRecord> pager;
	private Long[] minMaxIds = {null, null};

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
		pager = new Page<RegistryRecord>(500, 1);
		Long[] values = registryRecordDaoExt.getMinMaxIdsForImporting(registry.getId());
		minMaxIds[0] = values[0];
		minMaxIds[1] = values[1];

		log.info("Min and max are {}, {}", values[0], values[1]);

		Long minId = minMaxIds[0];
		Long maxId = minMaxIds[0] + pager.getPageSize();

		List<RegistryRecord> datum = registryRecordDaoExt.listRecordsForImport(registry.getId(), minId, maxId);
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

		if (pager.getThisPageLastElementNumber() >= minMaxIds[1]) {
			return false;
		}

		// get next page
		int nextPage = pager.getPageNumber() + 1;
		pager.setPageNumber(nextPage);

		Long minId = minMaxIds[0] + pager.getThisPageFirstElementNumber();
		Long maxId = minMaxIds[0] + pager.getThisPageLastElementNumber();

		List<RegistryRecord> datum = registryRecordDaoExt.listRecordsForImport(registry.getId(), minId, maxId);
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
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	protected RawConsumerData convert(RegistryRecord record) {
		record.setRegistry(registry);
		return RawConsumersDataUtil.convert(record);
	}

	public void setRegistryRecordDao(RegistryRecordDao registryRecordDao) {
		this.registryRecordDao = registryRecordDao;
	}

	public void setRegistryRecordDaoExt(RegistryRecordDaoExt registryRecordDaoExt) {
		this.registryRecordDaoExt = registryRecordDaoExt;
	}
}