package org.flexpay.eirc.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.ImportErrorDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.ObjectWithStatus;
import org.flexpay.eirc.dao.SpRegistryRecordDao;
import org.flexpay.eirc.dao.SpRegistryRecordDaoExt;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.SpRegistryRecordStatus;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.persistence.filters.RegistryRecordStatusFilter;
import org.flexpay.eirc.service.SpRegistryRecordService;
import org.flexpay.eirc.service.SpRegistryRecordStatusService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SpRegistryRecordServiceImpl implements SpRegistryRecordService {
	private static Logger log = Logger
			.getLogger(SpRegistryRecordServiceImpl.class);

	private SpRegistryRecordStatusService recordStatusService;
	private ImportErrorDao importErrorDao;
	private SpRegistryRecordDao spRegistryRecordDao;
	private SpRegistryRecordDaoExt spRegistryRecordDaoExt;

	/**
	 * Create SpRegistryRecord
	 *
	 * @param spRegistryRecord SpRegistryRecord object
	 * @return created SpRegistryRecord object
	 */
	@Transactional(readOnly = false)
	public SpRegistryRecord create(SpRegistryRecord spRegistryRecord) throws FlexPayException {
		spRegistryRecordDao.create(spRegistryRecord);

		if (log.isDebugEnabled()) {
			log.debug("Created SpRegistryRecord: " + spRegistryRecord);
		}

		return spRegistryRecord;
	}

	/**
	 * Read SpRegistryRecord object by its unique id
	 *
	 * @param id SpRegistryRecord key
	 * @return SpRegistryRecord object, or <code>null</code> if object not
	 *         found
	 */
	public SpRegistryRecord read(Long id) {
		return spRegistryRecordDao.readFull(id);
	}

	/**
	 * Update SpRegistryRecord
	 *
	 * @param spRegistryRecord SpRegistryRecord to update for
	 * @return Updated SpRegistryRecord object
	 * @throws FlexPayException if SpRegistryRecord object is invalid
	 */
	@Transactional(readOnly = false)
	public SpRegistryRecord update(SpRegistryRecord spRegistryRecord) throws FlexPayException {
		spRegistryRecordDao.update(spRegistryRecord);

		return spRegistryRecord;
	}

	@Transactional(readOnly = false)
	public void delete(SpRegistryRecord spRegistryRecord) {
		spRegistryRecordDao.delete(spRegistryRecord);
	}

	/**
	 * List registry records
	 *
	 * @param registry			  Registry to get records for
	 * @param importErrorTypeFilter Errors type filter
	 * @param recordStatusFilter	Records status filter
	 * @param pager				 Page
	 * @return list of filtered registry records
	 */
	@Transactional(readOnly = true)
	public List<SpRegistryRecord> listRecords(SpRegistry registry, ImportErrorTypeFilter importErrorTypeFilter,
											  RegistryRecordStatusFilter recordStatusFilter, Page<SpRegistryRecord> pager) {
		return spRegistryRecordDaoExt.filterRecords(registry.getId(), importErrorTypeFilter, recordStatusFilter, pager);
	}

	/**
	 * Count number of error in registry
	 *
	 * @param registry Registry to count errors for
	 * @return number of errors
	 */
	public int getErrorsNumber(SpRegistry registry) {
		return spRegistryRecordDaoExt.getErrorsNumber(registry.getId());
	}

	/**
	 * Find data source description for record
	 *
	 * @param record Registry record
	 * @return DataSourceDescription
	 */
	public DataSourceDescription getDataSourceDescription(SpRegistryRecord record) {
		return spRegistryRecordDaoExt.getDataSourceDescription(record.getId());
	}

	/**
	 * Set record status to fixed and invalidate error
	 *
	 * @param record Registry record
	 * @return updated record
	 */
	public SpRegistryRecord removeError(SpRegistryRecord record) throws FlexPayException {
		if (record.getImportError() == null) {
			return record;
		}

		SpRegistryRecordStatus status = recordStatusService.findByCode(SpRegistryRecordStatus.FIXED);
		if (status == null) {
			throw new FlexPayException("Registry record status FIXED was not found, was DB inited?");
		}

		// disable error
		ImportError error = record.getImportError();
		error.setStatus(ObjectWithStatus.STATUS_DISABLED);
		importErrorDao.update(error);

		// remove error and set status to FIXED
		record.setImportError(null);
		record.setRecordStatus(status);
		spRegistryRecordDao.update(record);

		return record;
	}

	/**
	 * @param spRegistryRecordDao the spRegistryRecordDao to set
	 */
	public void setSpRegistryRecordDao(SpRegistryRecordDao spRegistryRecordDao) {
		this.spRegistryRecordDao = spRegistryRecordDao;
	}

	public void setSpRegistryRecordDaoExt(SpRegistryRecordDaoExt spRegistryRecordDaoExt) {
		this.spRegistryRecordDaoExt = spRegistryRecordDaoExt;
	}
}
