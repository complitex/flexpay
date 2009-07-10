package org.flexpay.common.service.imp;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.registry.RegistryRecordContainerDao;
import org.flexpay.common.dao.registry.RegistryRecordDao;
import org.flexpay.common.dao.registry.RegistryRecordDaoExt;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordContainer;
import org.flexpay.common.persistence.registry.workflow.RegistryRecordWorkflowManager;
import org.flexpay.common.service.RegistryRecordService;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class RegistryRecordServiceImpl implements RegistryRecordService {

	private final Logger log = LoggerFactory.getLogger(getClass());

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
	@Transactional (readOnly = false)
	public RegistryRecord create(RegistryRecord record) {
		registryRecordDao.create(record);

		for (RegistryRecordContainer container : record.getContainers()) {
			recordContainerDao.create(container);
		}

		return record;
	}

	/**
	 * Batch create records
	 *
	 * @param records Collection of records to create
	 */
	@Override
	@Transactional (readOnly = false)
	public void create(Collection<RegistryRecord> records) {
		for (RegistryRecord record : records) {
			create(record);
		}
	}

	/**
	 * Read RegistryRecord object by its unique id
	 *
	 * @param id RegistryRecord key
	 * @return RegistryRecord object, or <code>null</code> if object not found
	 */
	@Nullable
	public RegistryRecord read(Long id) {
		return registryRecordDao.readFull(id);
	}

	/**
	 * Update SpRegistryRecord
	 *
	 * @param spRegistryRecord SpRegistryRecord to update for
	 * @return Updated SpRegistryRecord object
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if SpRegistryRecord object is invalid
	 */
	@Transactional (readOnly = false)
	public RegistryRecord update(RegistryRecord spRegistryRecord) throws FlexPayException {
		registryRecordDao.update(spRegistryRecord);

		return spRegistryRecord;
	}

	@Transactional (readOnly = false)
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
	public List<RegistryRecord> listRecords(Registry registry, ImportErrorTypeFilter importErrorTypeFilter,
											RegistryRecordStatusFilter recordStatusFilter, Page<RegistryRecord> pager) {
		return registryRecordDaoExt.filterRecords(registry.getId(), importErrorTypeFilter, recordStatusFilter, pager);
	}

	public List<RegistryRecord> listRecordsForExport(Registry registry, FetchRange range) {
		return registryRecordDao.listRecordsForExport(registry.getId(), range);
	}

	/**
	 * Count number of error in registry
	 *
	 * @param registry Registry to count errors for
	 * @return number of errors
	 */
	public int getErrorsNumber(Registry registry) {
		return registryRecordDaoExt.getErrorsNumber(registry.getId());
	}

	/**
	 * Set record status to fixed and invalidate error
	 *
	 * @param record Registry record
	 * @return updated record
	 */
	@Transactional (readOnly = false)
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
	public Collection<RegistryRecord> findObjects(Registry registry, Set<Long> objectIds) {
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

	@Required
	public void setSpRegistryRecordDao(RegistryRecordDao registryRecordDao) {
		this.registryRecordDao = registryRecordDao;
	}

	@Required
	public void setSpRegistryRecordDaoExt(RegistryRecordDaoExt registryRecordDaoExt) {
		this.registryRecordDaoExt = registryRecordDaoExt;
	}

	@Required
	public void setWorkflowManager(RegistryRecordWorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	@Required
	public void setRecordContainerDao(RegistryRecordContainerDao recordContainerDao) {
		this.recordContainerDao = recordContainerDao;
	}

}
