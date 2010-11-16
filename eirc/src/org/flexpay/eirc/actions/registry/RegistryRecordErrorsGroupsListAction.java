package org.flexpay.eirc.actions.registry;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.ab.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.common.persistence.registry.RecordErrorsGroup;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.sorter.RecordErrorsGroupSorter;
import org.flexpay.common.persistence.registry.sorter.RecordErrorsGroupSorterByName;
import org.flexpay.common.persistence.registry.sorter.RecordErrorsGroupSorterByNumberOfErrors;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.actions.registry.data.RecordErrorsGroupView;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class RegistryRecordErrorsGroupsListAction extends AccountantAWPWithPagerActionSupport<RecordErrorsGroup> {

    private Registry registry = new Registry();
    private List<RecordErrorsGroupView> errorGroups = list();
    private RecordErrorsGroupSorterByName recordErrorsGroupSorterByName = new RecordErrorsGroupSorterByName();
    private RecordErrorsGroupSorterByNumberOfErrors recordErrorsGroupSorterByNumberOfErrors = new RecordErrorsGroupSorterByNumberOfErrors();

    protected ImportErrorTypeFilter importErrorTypeFilter = null;

    private RegistryRecordService registryRecordService;
    private ClassToTypeRegistry typeRegistry;

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

        if (importErrorTypeFilter == null) {
            importErrorTypeFilter = new ImportErrorTypeFilter();
        }

        importErrorTypeFilter.init(typeRegistry);

        if (log.isDebugEnabled()) {
            watch.stop();
            log.debug("Import error type filter init: {}", watch);
            watch.reset();
        }

        if (log.isDebugEnabled()) {
            watch.start();
        }

        RecordErrorsGroupSorter sorter;
        if (recordErrorsGroupSorterByName.isActivated()) {
            sorter = recordErrorsGroupSorterByName;
        } else if (recordErrorsGroupSorterByNumberOfErrors.isActivated()) {
            sorter = recordErrorsGroupSorterByNumberOfErrors;
        } else {
            recordErrorsGroupSorterByNumberOfErrors.activate();
            recordErrorsGroupSorterByNumberOfErrors.setOrder(ObjectSorter.ORDER_DESC);
            sorter = recordErrorsGroupSorterByNumberOfErrors;
        }

        List<RecordErrorsGroup> groups = registryRecordService.listRecordErrorsGroups(registry, sorter, importErrorTypeFilter, importErrorTypeFilter.getGroupByString(), getPager());
        errorGroups = list();

        for (RecordErrorsGroup group : groups) {
            errorGroups.add(new RecordErrorsGroupView(group, typeRegistry));
        }

        if (log.isDebugEnabled()) {
            watch.stop();
            log.debug("Time spent listing errors groups: {}", watch);
            log.debug("Total errors groups found: {}", errorGroups.size());
        }

        return SUCCESS;
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

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public void setImportErrorTypeFilter(ImportErrorTypeFilter importErrorTypeFilter) {
        this.importErrorTypeFilter = importErrorTypeFilter;
    }

    public List<RecordErrorsGroupView> getErrorGroups() {
        return errorGroups;
    }

    public RecordErrorsGroupSorterByName getRecordErrorsGroupSorterByName() {
        return recordErrorsGroupSorterByName;
    }

    public void setRecordErrorsGroupSorterByName(RecordErrorsGroupSorterByName recordErrorsGroupSorterByName) {
        this.recordErrorsGroupSorterByName = recordErrorsGroupSorterByName;
    }

    public RecordErrorsGroupSorterByNumberOfErrors getRecordErrorsGroupSorterByNumberOfErrors() {
        return recordErrorsGroupSorterByNumberOfErrors;
    }

    public void setRecordErrorsGroupSorterByNumberOfErrors(RecordErrorsGroupSorterByNumberOfErrors recordErrorsGroupSorterByNumberOfErrors) {
        this.recordErrorsGroupSorterByNumberOfErrors = recordErrorsGroupSorterByNumberOfErrors;
    }

    @Required
    public void setRegistryRecordService(RegistryRecordService registryRecordService) {
        this.registryRecordService = registryRecordService;
    }

    @Required
    public void setTypeRegistry(ClassToTypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
    }
}
