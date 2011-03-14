package org.flexpay.payments.action.service;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.DataSourceFilter;
import org.flexpay.common.service.DataSourceDescriptionService;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.ServiceTypeService;
import org.flexpay.payments.service.importexport.impl.ClassToTypeRegistryPayments;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.flexpay.common.persistence.Stub.stub;

public class ServiceTypeCorrectionCreateAction extends FPActionSupport {

    private DataCorrection dataCorrection = new DataCorrection();
    private ServiceType serviceType = new ServiceType();
    private DataSourceFilter dataSourceFilter = new DataSourceFilter();

    private CorrectionsService correctionsService;
    private DataSourceDescriptionService dataSourceDescriptionService;
    private ServiceTypeService serviceTypeService;
    private ClassToTypeRegistryPayments classToTypeRegistryPayments;

    @NotNull
    @Override
    public String doExecute() throws Exception {

        if (dataCorrection == null) {
            log.warn("Data correction parameter is null");
            addActionError(getText("payments.error.service_type.incorrect_data_correction"));
            return REDIRECT_SUCCESS;
        }

        if (dataCorrection.isNotNew()) {
            log.warn("We can't edit Data correction! But we have data correction with id {}", dataCorrection.getId());
            addActionError(getText("payments.error.service_type.incorrect_data_correction"));
            return REDIRECT_SUCCESS;
        }

        if (serviceType == null || serviceType.isNew()) {
            log.warn("Incorrect service type id");
            addActionError(getText("payments.error.service_type.incorrect_service_type_id"));
            return REDIRECT_SUCCESS;
        }

        Stub<ServiceType> stub = stub(serviceType);
        serviceType = serviceTypeService.read(stub);
        if (serviceType == null) {
            log.warn("Can't get service type with id {} from DB", stub.getId());
            addActionError(getText("payments.error.service_type.cant_get_service_type"));
            return REDIRECT_SUCCESS;
        } else if (serviceType.isNotActive()) {
            log.warn("Service type with id {} is disabled", stub.getId());
            addActionError(getText("payments.error.service_type.cant_get_service_type"));
            return REDIRECT_SUCCESS;
        }

        initData();
        initFilters();

        if (isSubmit()) {
            if (!doValidate()) {
                return INPUT;
            }

            dataCorrection.setDataSourceDescription(dataSourceFilter.getSelected());
            correctionsService.save(dataCorrection);

            addActionMessage(getText("payments.service_type.corection_added"));

            return REDIRECT_SUCCESS;
        }

        return INPUT;
    }

    private boolean doValidate() {

        Stub<DataSourceDescription> stub = new Stub<DataSourceDescription>(dataSourceFilter.getSelectedId());
        DataSourceDescription dataSourceDescription = dataSourceDescriptionService.read(stub);
        if (dataSourceDescription == null) {
            log.warn("Can't get data source description with id {} from DB", stub.getId());
            addActionError(getText("payments.error.service_type.cant_get_data_source"));
        }

        if (isEmpty(dataCorrection.getExternalId())) {
            log.warn("Incorrect external id value {}", dataCorrection.getExternalId());
            addActionError(getText("payments.error.service_type.incorrect_external_id"));
        }

        if (dataCorrection.getInternalObjectId() == null || dataCorrection.getInternalObjectId() <= 0) {
            log.warn("Incorrect internal id value {}", dataCorrection.getInternalObjectId());
            addActionError(getText("payments.error.service_type.incorrect_internal_id"));
        }

        return !hasActionErrors();
    }

    private void initFilters() throws Exception {

        if (dataSourceFilter == null) {
            log.warn("dataSourceFilter parameter is null");
            dataSourceFilter = new DataSourceFilter();
        }

        dataSourceFilter.setAllowEmpty(false);
        dataSourceFilter = dataSourceDescriptionService.initDataSourceFilter(dataCorrection.getInternalObjectId(), dataCorrection.getObjectType(), dataSourceFilter);
    }

    private void initData() {

        int type = classToTypeRegistryPayments.getType(ServiceType.class);
        dataCorrection.setObjectType(type);
        dataCorrection.setInternalObjectId(serviceType.getId());

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
        return INPUT;
    }

    public void setDataCorrection(DataCorrection dataCorrection) {
        this.dataCorrection = dataCorrection;
    }

    public DataCorrection getDataCorrection() {
        return dataCorrection;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public DataSourceFilter getDataSourceFilter() {
        return dataSourceFilter;
    }

    @Required
    public void setServiceTypeService(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @Required
    public void setClassToTypeRegistryPayments(ClassToTypeRegistryPayments classToTypeRegistryPayments) {
        this.classToTypeRegistryPayments = classToTypeRegistryPayments;
    }

    @Required
    public void setCorrectionsService(CorrectionsService correctionsService) {
        this.correctionsService = correctionsService;
    }

    @Required
    public void setDataSourceDescriptionService(DataSourceDescriptionService dataSourceDescriptionService) {
        this.dataSourceDescriptionService = dataSourceDescriptionService;
    }
}
