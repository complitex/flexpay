package org.flexpay.payments.actions.service;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class ServiceTypeCorrectionsListPageAction extends FPActionSupport {

    private ServiceType serviceType = new ServiceType();

    private ServiceTypeService serviceTypeService;

    @NotNull
    @Override
    public String doExecute() throws Exception {

        if (serviceType == null || serviceType.isNew()) {
            log.warn("Incorrect service type id");
            addActionError(getText("payments.error.service_type.incorrect_service_type_id"));
            return REDIRECT_ERROR;
        }

        Stub<ServiceType> stub = stub(serviceType);
        serviceType = serviceTypeService.read(stub);
        if (serviceType == null) {
            log.warn("Can't get service type with id {} from DB", stub.getId());
            addActionError(getText("payments.error.service_type.cant_get_service_type"));
            return REDIRECT_ERROR;
        } else if (serviceType.isNotActive()) {
            log.warn("Service type with id {} is disabled", stub.getId());
            addActionError(getText("payments.error.service_type.cant_get_service_type"));
            return REDIRECT_ERROR;
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
        return REDIRECT_ERROR;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    @Required
    public void setServiceTypeService(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

}
