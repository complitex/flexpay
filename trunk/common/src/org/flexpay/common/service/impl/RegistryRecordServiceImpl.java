package org.flexpay.common.service.impl;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.registry.RegistryRecordContainerDao;
import org.flexpay.common.dao.registry.RegistryRecordDao;
import org.flexpay.common.dao.registry.RegistryRecordDaoExt;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.persistence.registry.filter.FilterData;
import org.flexpay.common.persistence.registry.sorter.RecordErrorsGroupSorter;
import org.flexpay.common.persistence.registry.workflow.RegistryRecordWorkflowManager;
import org.flexpay.common.service.RegistryRecordService;
import org.jetbrains.annotations.NotNull;
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
	@Override
	public RegistryRecord create(RegistryRecord record) {

		registryRecordDao.create(record);

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
	 * @param stub RegistryRecord stub
	 * @return RegistryRecord object, or <code>null</code> if object not found
	 */
	@Nullable
	@Override
	public RegistryRecord read(@NotNull Stub<RegistryRecord> stub) {
		return registryRecordDao.readFull(stub.getId());
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
	@Override
	public RegistryRecord update(RegistryRecord spRegistryRecord) throws FlexPayException {
		registryRecordDao.update(spRegistryRecord);

		return spRegistryRecord;
	}

	@Transactional (readOnly = false)
	@Override
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
	@Override
	public List<RegistryRecord> listRecords(Registry registry, ImportErrorTypeFilter importErrorTypeFilter,
											RegistryRecordStatusFilter recordStatusFilter, Page<RegistryRecord> pager) {
		return registryRecordDaoExt.filterRecords(registry.getId(), importErrorTypeFilter, recordStatusFilter, pager);
	}

    @Override
    public List<RegistryRecord> listRecords(Registry registry, Collection<ObjectFilter> filters, Page<RegistryRecord> pager) {
        return registryRecordDaoExt.filterRecords(registry.getId(), filters, pager);
    }

    @Override
    public List<String> listAutocompleterAddresses(Registry registry, FilterData filterData, Page<String> pager) {
        return registryRecordDaoExt.findAutocompleterAddresses(registry.getId(), filterData, pager);
    }

    @Override
    public List<RegistryRecord> listRecords(Registry registry, Collection<ObjectFilter> filters,
                                            String criteria, List<Object> params, Page<RegistryRecord> pager) {
        return registryRecordDaoExt.filterRecords(registry.getId(), filters, criteria, params, pager);
    }

    @Override
    public List<RegistryRecord> listRecords(RegistryRecord record, String correctionType, Page<RegistryRecord> pager) {
        return registryRecordDaoExt.findRecordsWithThisError(record, correctionType, pager);
    }

    @Override
	public List<RegistryRecord> listRecordsForExport(Registry registry, FetchRange range) {
		return registryRecordDao.listRecordsForExport(registry.getId(), range);
	}

    @Override
    public List<RecordErrorsGroup> listRecordErrorsGroups(@NotNull Registry registry, RecordErrorsGroupSorter sorter, Collection<ObjectFilter> filters, String groupByString, Page<RecordErrorsGroup> pager) {
        return registryRecordDaoExt.findErrorsGroups(registry.getId(), sorter, filters, groupByString, pager);
    }

    @Override
    public List<RecordErrorsType> listRecordErrorsTypes(@NotNull Registry registry, Collection<ObjectFilter> filters) {
        return registryRecordDaoExt.findErrorsTypes(registry.getId(), filters);
    }

    /**
	 * Count number of error in registry
	 *
	 * @param registry Registry to count errors for
	 * @return number of errors
	 */
	@Override
	public int getErrorsNumber(Registry registry) {
		log.debug("Getting registry errors number: {}", registry.getId());
		return registryRecordDaoExt.getErrorsNumber(registry.getId());
	}

	/**
	 * Set record status to fixed and invalidate error
	 *
	 * @param record Registry record
	 * @return updated record
	 */
	@Transactional (readOnly = false)
	@Override
	public RegistryRecord removeError(RegistryRecord record) throws Exception {
		workflowManager.setNextSuccessStatus(record);
		return record;
	}

    @Transactional (readOnly = false)
    @Override
    public void removeError(Collection<RegistryRecord> records) throws Exception {
        workflowManager.setNextStatusForErrorRecords(records);
    }

    /**
	 * Find registry records by identifiers
	 *
	 * @param registry  Registry to get records for
	 * @param objectIds Set of identifiers
	 * @return Records
	 */
	@Override
	public Collection<RegistryRecord> findObjects(Registry registry, Set<Long> objectIds) {
		return registryRecordDaoExt.findRecords(registry.getId(), objectIds);
	}

	/**
	 * Find containers associated with a registry record
	 *
	 * @param stub Registry record stub
	 * @return List of containers
	 */
	@Override
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
