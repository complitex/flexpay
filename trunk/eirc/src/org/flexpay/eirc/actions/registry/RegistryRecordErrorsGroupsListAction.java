package org.flexpay.eirc.actions.registry;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.ab.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.RecordErrorsGroup;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.persistence.registry.filter.StringFilter;
import org.flexpay.common.persistence.registry.sorter.RecordErrorsGroupSorter;
import org.flexpay.common.persistence.registry.sorter.RecordErrorsGroupSorterByName;
import org.flexpay.common.persistence.registry.sorter.RecordErrorsGroupSorterByNumberOfErrors;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryRecordStatusService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.actions.registry.data.RecordErrorsGroupView;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.flexpay.common.persistence.registry.RegistryRecordStatus.PROCESSED_WITH_ERROR;
import static org.flexpay.common.persistence.registry.filter.StringFilter.*;
import static org.flexpay.common.util.CollectionUtils.list;

public class RegistryRecordErrorsGroupsListAction extends AccountantAWPWithPagerActionSupport<RecordErrorsGroup> {

    private Registry registry = new Registry();
    private List<RecordErrorsGroupView> errorGroups = list();
    private RecordErrorsGroupSorterByName recordErrorsGroupSorterByName = new RecordErrorsGroupSorterByName();
    private RecordErrorsGroupSorterByNumberOfErrors recordErrorsGroupSorterByNumberOfErrors = new RecordErrorsGroupSorterByNumberOfErrors();

    protected ImportErrorTypeFilter importErrorTypeFilter = null;
    private StringFilter townFilter = new StringFilter();
    private StringFilter streetFilter = new StringFilter();
    private StringFilter buildingFilter = new StringFilter();
    private StringFilter apartmentFilter = new StringFilter();
    private StringFilter fioFilter = new StringFilter();

    private RegistryRecordStatusService registryRecordStatusService;
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

        List<ObjectFilter> filters = initFilters();
        if (filters == null) {
            return SUCCESS;
        }

        if (log.isDebugEnabled()) {
            watch.stop();
            log.debug("Filters init: {}", watch);
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

        List<RecordErrorsGroup> groups = registryRecordService.listRecordErrorsGroups(registry, sorter, filters, importErrorTypeFilter.getGroupByString(), getPager());
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

        if (importErrorTypeFilter == null) {
            importErrorTypeFilter = new ImportErrorTypeFilter();
        }

        importErrorTypeFilter.init(typeRegistry);

        filters.add(importErrorTypeFilter);

        if (isNotEmpty(townFilter.getValue())) {
            townFilter.setType(TYPE_TOWN);
            townFilter.setValue("%" + townFilter.getValue() + "%");
            filters.add(townFilter);
        }
        if (isNotEmpty(streetFilter.getValue())) {
            streetFilter.setType(TYPE_STREET);
            streetFilter.setValue("%" + streetFilter.getValue() + "%");
            filters.add(streetFilter);
        }
        if (isNotEmpty(buildingFilter.getValue())) {
            buildingFilter.setType(TYPE_BUILDING);
            buildingFilter.setValue(buildingFilter.getValue() + "%");
            filters.add(buildingFilter);
        }
        if (isNotEmpty(apartmentFilter.getValue())) {
            apartmentFilter.setType(TYPE_APARTMENT);
            filters.add(apartmentFilter);
        }
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

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public void setImportErrorTypeFilter(ImportErrorTypeFilter importErrorTypeFilter) {
        this.importErrorTypeFilter = importErrorTypeFilter;
    }

    public void setTownFilter(StringFilter townFilter) {
        this.townFilter = townFilter;
    }

    public void setStreetFilter(StringFilter streetFilter) {
        this.streetFilter = streetFilter;
    }

    public void setBuildingFilter(StringFilter buildingFilter) {
        this.buildingFilter = buildingFilter;
    }

    public void setApartmentFilter(StringFilter apartmentFilter) {
        this.apartmentFilter = apartmentFilter;
    }

    public void setFioFilter(StringFilter fioFilter) {
        this.fioFilter = fioFilter;
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

    @Required
    public void setRegistryRecordStatusService(RegistryRecordStatusService registryRecordStatusService) {
        this.registryRecordStatusService = registryRecordStatusService;
    }

}
