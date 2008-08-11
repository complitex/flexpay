package org.flexpay.eirc.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.eirc.dao.RegistryRecordDao;
import org.flexpay.eirc.dao.RegistryRecordDaoExt;
import org.flexpay.eirc.dao.RegistryRecordContainerDao;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.RegistryRecord;
import org.flexpay.eirc.persistence.RegistryRecordContainer;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.persistence.filters.RegistryRecordStatusFilter;
import org.flexpay.eirc.persistence.workflow.RegistryRecordWorkflowManager;
import org.flexpay.eirc.service.SpRegistryRecordService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public class SpRegistryRecordServiceImpl implements SpRegistryRecordService {
	private final Logger log = Logger.getLogger(getClass());

	private RegistryRecordContainerDao recordContainerDao;
	private RegistryRecordDao registryRecordDao;
	private RegistryRecordDaoExt registryRecordDaoExt;

	private RegistryRecordWorkflowManager workflowManager;

	/**
	 * Create SpRegistryRecord
	 *
	 * @param record SpRegistryRecord object
	 * @return created SpRegistryRecord object
	 */
	@Transactional(readOnly = false)
	public RegistryRecord create(RegistryRecord record) throws FlexPayException {
		registryRecordDao.create(record);

		for (RegistryRecordContainer container : record.getContainers()) {
			recordContainerDao.create(container);
		}

		if (log.isDebugEnabled()) {
			log.debug("Created SpRegistryRecord: " + record);
		}

		return record;
	}

	/**
	 * Read SpRegistryRecord object by its unique id
	 *
	 * @param id SpRegistryRecord key
	 * @return SpRegistryRecord object, or <code>null</code> if object not
	 *         found
	 */
	public RegistryRecord read(Long id) {
		return registryRecordDao.readFull(id);
	}

	/**
	 * Update SpRegistryRecord
	 *
	 * @param spRegistryRecord SpRegistryRecord to update for
	 * @return Updated SpRegistryRecord object
	 * @throws FlexPayException if SpRegistryRecord object is invalid
	 */
	@Transactional(readOnly = false)
	public RegistryRecord update(RegistryRecord spRegistryRecord) throws FlexPayException {
		registryRecordDao.update(spRegistryRecord);

		return spRegistryRecord;
	}

	@Transactional(readOnly = false)
	public void delete(RegistryRecord spRegistryRecord) {
		registryRecordDao.delete(spRegistryRecord);
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
	public List<RegistryRecord> listRecords(SpRegistry registry, ImportErrorTypeFilter importErrorTypeFilter,
											  RegistryRecordStatusFilter recordStatusFilter, Page<RegistryRecord> pager) {
		return registryRecordDaoExt.filterRecords(registry.getId(), importErrorTypeFilter, recordStatusFilter, pager);
	}

	/**
	 * Count number of error in registry
	 *
	 * @param registry Registry to count errors for
	 * @return number of errors
	 */
	public int getErrorsNumber(SpRegistry registry) {
		return registryRecordDaoExt.getErrorsNumber(registry.getId());
	}

	/**
	 * Find data source description for record
	 *
	 * @param record Registry record
	 * @return DataSourceDescription
	 */
	public DataSourceDescription getDataSourceDescription(RegistryRecord record) {
		DataSourceDescription sd = registryRecordDaoExt.getDataSourceDescription(record.getId());

		if (log.isDebugEnabled()) {
			log.debug("Record Data source: " + sd);
		}

		return sd;
	}

	/**
	 * Set record status to fixed and invalidate error
	 *
	 * @param record Registry record
	 * @return updated record
	 */
	@Transactional(readOnly = false)
	public RegistryRecord removeError(RegistryRecord record) throws Exception {

		workflowManager.setNextSuccessStatus(record);
		return record;
	}

	/**
	 * Find registry records by identifiers
	 *
	 * @param registry  Registry to get records for
	 * @param objectIds Set of identifiers
	 * @return Records
	 */
	public Collection<RegistryRecord> findObjects(SpRegistry registry, Set<Long> objectIds) {
		return registryRecordDaoExt.findRecords(registry.getId(), objectIds);
	}

	/**
	 * Find containers associated with a registry record
	 *
	 * @param stub Registry record stub
	 * @return List of containers
	 */
	public List<RegistryRecordContainer> getRecordContainers(RegistryRecord stub) {
		return recordContainerDao.findRecordContainers(stub.getId());
	}

	/**
	 * @param registryRecordDao the spRegistryRecordDao to set
	 */
	public void setSpRegistryRecordDao(RegistryRecordDao registryRecordDao) {
		this.registryRecordDao = registryRecordDao;
	}

	public void setSpRegistryRecordDaoExt(RegistryRecordDaoExt registryRecordDaoExt) {
		this.registryRecordDaoExt = registryRecordDaoExt;
	}

	public void setWorkflowManager(RegistryRecordWorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	public void setRecordContainerDao(RegistryRecordContainerDao recordContainerDao) {
		this.recordContainerDao = recordContainerDao;
	}
}
