package org.flexpay.eirc.action.registry;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.filters.DistrictFilter;
import org.flexpay.ab.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.filter.StringValueFilter;
import org.flexpay.common.persistence.registry.RecordErrorsGroup;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.persistence.registry.sorter.RecordErrorsGroupSorter;
import org.flexpay.common.persistence.registry.sorter.RecordErrorsGroupSorterByName;
import org.flexpay.common.persistence.registry.sorter.RecordErrorsGroupSorterByNumberOfErrors;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryRecordStatusService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.payments.action.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.action.registry.data.RecordErrorsGroupView;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultTownStub;
import static org.flexpay.common.persistence.filter.StringValueFilter.*;
import static org.flexpay.common.persistence.registry.RegistryRecordStatus.PROCESSED_WITH_ERROR;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;

public class RegistryRecordErrorsGroupsListAction extends AccountantAWPWithPagerActionSupport<RecordErrorsGroup> {

    private Registry registry = new Registry();
    private List<RecordErrorsGroupView> errorGroups = list();
    private RecordErrorsGroupSorterByName recordErrorsGroupSorterByName = new RecordErrorsGroupSorterByName();
    private RecordErrorsGroupSorterByNumberOfErrors recordErrorsGroupSorterByNumberOfErrors = new RecordErrorsGroupSorterByNumberOfErrors();

    private Map<Long, String> names = treeMap();
    private Map<Long, String> shortNames = treeMap();

    protected DistrictFilter districtFilter = new DistrictFilter();

    protected ImportErrorTypeFilter importErrorTypeFilter = null;
    private StringValueFilter townFilter = new StringValueFilter();
    private StringValueFilter streetFilter = new StringValueFilter();
    private StringValueFilter buildingFilter = new StringValueFilter();
    private StringValueFilter apartmentFilter = new StringValueFilter();
    private StringValueFilter fioFilter = new StringValueFilter();

    private RegistryRecordStatusService registryRecordStatusService;
    private RegistryRecordService registryRecordService;
    private ClassToTypeRegistry typeRegistry;
    private ParentService<DistrictFilter> parentService;

    @NotNull
    @Override
    public String doExecute() throws Exception {

        if (registry == null || registry.isNew()) {
            log.warn("Incorrect registry id");
            addActionError(getText("payments.error.registry.incorrect_registry_id"));
            return SUCCESS;
        }

        districtFilter = parentService.initFilter(districtFilter, new TownFilter(getDefaultTownStub()), getUserPreferences().getLocale());

        StopWatch watch = new StopWatch();
        if (log.isDebugEnabled()) {
            watch.start();
        }

        correctNames();

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

    private void correctNames() {
        Map<Long, String> newNames = treeMap();
        Map<Long, String> newShortNames = treeMap();
        for (Language lang : getLanguages()) {
            newNames.put(lang.getId(), names != null && names.containsKey(lang.getId()) ? names.get(lang.getId()) : "");
            newShortNames.put(lang.getId(), shortNames != null && shortNames.containsKey(lang.getId()) ? shortNames.get(lang.getId()) : "");
        }
        names = newNames;
        shortNames = newShortNames;
    }

    public boolean checkApartmentType(int i) {
        return typeRegistry.getType(Apartment.class) == errorGroups.get(i).getGroup().getErrorType();
    }

    public boolean checkBuildingType(int i) {
        return typeRegistry.getType(BuildingAddress.class) == errorGroups.get(i).getGroup().getErrorType();
    }

    public boolean checkStreetType(int i) {
        return typeRegistry.getType(Street.class) == errorGroups.get(i).getGroup().getErrorType();
    }

    public boolean checkStreetTypeType(int i) {
        return typeRegistry.getType(StreetType.class) == errorGroups.get(i).getGroup().getErrorType();
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

    public void setTownFilter(StringValueFilter townFilter) {
        this.townFilter = townFilter;
    }

    public void setStreetFilter(StringValueFilter streetFilter) {
        this.streetFilter = streetFilter;
    }

    public void setBuildingFilter(StringValueFilter buildingFilter) {
        this.buildingFilter = buildingFilter;
    }

    public void setApartmentFilter(StringValueFilter apartmentFilter) {
        this.apartmentFilter = apartmentFilter;
    }

    public void setFioFilter(StringValueFilter fioFilter) {
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

    public Map<Long, String> getNames() {
        return names;
    }

    public Map<Long, String> getShortNames() {
        return shortNames;
    }

    public DistrictFilter getDistrictFilter() {
        return districtFilter;
    }

    public void setDistrictFilter(DistrictFilter districtFilter) {
        this.districtFilter = districtFilter;
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

    @Required
    public void setParentService(ParentService<DistrictFilter> parentService) {
        this.parentService = parentService;
    }
}
