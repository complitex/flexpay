package org.flexpay.eirc.action.registry;

import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.filter.StringValueFilter;
import org.flexpay.common.persistence.registry.RecordErrorsGroup;
import org.flexpay.common.persistence.registry.RecordErrorsType;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryRecordStatusService;
import org.flexpay.payments.action.AccountantAWPWithPagerActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.flexpay.common.persistence.filter.StringValueFilter.*;
import static org.flexpay.common.persistence.registry.RegistryRecordStatus.PROCESSED_WITH_ERROR;
import static org.flexpay.common.util.CollectionUtils.list;

public class RegistryRecordErrorsTypesListAction extends AccountantAWPWithPagerActionSupport<RecordErrorsGroup> {

    private Registry registry = new Registry();
    private List<RecordErrorsType> errorsTypes = list();

    private StringValueFilter townFilter = new StringValueFilter();
    private StringValueFilter streetFilter = new StringValueFilter();
    private StringValueFilter buildingFilter = new StringValueFilter();
    private StringValueFilter apartmentFilter = new StringValueFilter();
    private StringValueFilter fioFilter = new StringValueFilter();

    private RegistryRecordStatusService registryRecordStatusService;
    private RegistryRecordService registryRecordService;

    @NotNull
    @Override
    public String doExecute() throws Exception {

        if (registry == null || registry.isNew()) {
            log.warn("Incorrect registry id");
            addActionError(getText("payments.error.registry.incorrect_registry_id"));
            return SUCCESS;
        }

        List<ObjectFilter> filters = initFilters();
        if (filters == null) {
            return SUCCESS;
        }

        errorsTypes = registryRecordService.listRecordErrorsTypes(registry, filters);

        if (log.isDebugEnabled()) {
            log.debug("Total errors types found: {}", errorsTypes.size());
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

    public List<RecordErrorsType> getErrorsTypes() {
        return errorsTypes;
    }

    @Required
    public void setRegistryRecordService(RegistryRecordService registryRecordService) {
        this.registryRecordService = registryRecordService;
    }

    @Required
    public void setRegistryRecordStatusService(RegistryRecordStatusService registryRecordStatusService) {
        this.registryRecordStatusService = registryRecordStatusService;
    }
}
