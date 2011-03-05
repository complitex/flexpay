package org.flexpay.eirc.actions.registry;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.filter.StringValueFilter;
import org.flexpay.common.persistence.registry.RecordErrorsGroup;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryRecordStatusService;
import org.flexpay.common.service.impl.fetch.ProcessingReadHintsHandlerFactory;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.actions.registry.data.RecordErrorsGroupView;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.flexpay.common.persistence.filter.StringValueFilter.TYPE_FIO;
import static org.flexpay.common.persistence.registry.RegistryRecordStatus.PROCESSED_WITH_ERROR;
import static org.flexpay.common.util.CollectionUtils.list;

public class RegistryRecordsListSimpleAction extends AccountantAWPWithPagerActionSupport<RegistryRecord> {

    private Long index;
	private Registry registry = new Registry();
    private RecordErrorsGroup group = new RecordErrorsGroup();
	private List<RegistryRecord> records = list();

    private StringValueFilter fioFilter = new StringValueFilter();
    
	private RegistryRecordService registryRecordService;
    private RegistryRecordStatusService registryRecordStatusService;
	private ClassToTypeRegistry classToTypeRegistry;

	private ProcessingReadHintsHandlerFactory hintsHandlerFactory = null;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (registry == null || registry.isNew()) {
			log.warn("Incorrect registry id");
			addActionError(getText("payments.error.registry.incorrect_registry_id"));
			return SUCCESS;
		}

		StopWatch watch = new StopWatch();
		if (log.isDebugEnabled()) {
			watch.start();
		}

        List<ObjectFilter> filters = initFilters();
        if (filters == null) {
            return SUCCESS;
        }

		if (log.isDebugEnabled()) {
			watch.stop();
			log.debug("Filters init: {}", watch);
			watch.reset();
			watch.start();
		}

        RecordErrorsGroupView groupView = new RecordErrorsGroupView(group, classToTypeRegistry);

		records = registryRecordService.listRecords(registry, filters, groupView.getCriteria(classToTypeRegistry), groupView.getParams(classToTypeRegistry), getPager());

		if (hintsHandlerFactory != null && !records.isEmpty()) {
			log.debug("select consumers with eirc account for registry records");
			hintsHandlerFactory.getInstance(null, null, records).read();
		}

		if (log.isDebugEnabled()) {
			watch.stop();
			log.debug("Time spent listing records: {}", watch);
			log.debug("Total records found: {}", records.size());
		}

		return SUCCESS;
	}

    private List<ObjectFilter> initFilters() {

        List<ObjectFilter> filters = list();

        RegistryRecordStatusFilter recordStatusFilter = new RegistryRecordStatusFilter();
        RegistryRecordStatus status = registryRecordStatusService.findByCode(PROCESSED_WITH_ERROR);
        if (status == null) {
            log.warn("Can't get status by code from DB ({})", PROCESSED_WITH_ERROR);
            return null;
        }
        recordStatusFilter.setSelectedId(status.getId());
        filters.add(recordStatusFilter);

        ImportErrorTypeFilter importErrorTypeFilter = new ImportErrorTypeFilter();
        importErrorTypeFilter.setSelectedType(group.getErrorType());
        importErrorTypeFilter.init(classToTypeRegistry);

        filters.add(importErrorTypeFilter);

        if (isNotEmpty(fioFilter.getValue())) {
            fioFilter.setType(TYPE_FIO);
            fioFilter.setValue("%" + fioFilter.getValue() + "%");
            filters.add(fioFilter);
        }

        return filters;

    }

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

    public RecordErrorsGroup getGroup() {
        return group;
    }

    public void setGroup(RecordErrorsGroup group) {
        this.group = group;
    }

    public void setFioFilter(StringValueFilter fioFilter) {
        this.fioFilter = fioFilter;
    }

	public List<RegistryRecord> getRecords() {
		return records;
	}

    @Required
	public void setRegistryRecordService(RegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

    @Required
    public void setRegistryRecordStatusService(RegistryRecordStatusService registryRecordStatusService) {
        this.registryRecordStatusService = registryRecordStatusService;
    }

    @Required
	public void setClassToTypeRegistry(ClassToTypeRegistry classToTypeRegistry) {
		this.classToTypeRegistry = classToTypeRegistry;
	}

	public void setHintsHandlerFactory(ProcessingReadHintsHandlerFactory hintsHandlerFactory) {
		this.hintsHandlerFactory = hintsHandlerFactory;
	}
}
