package org.flexpay.orgs.action.serviceprovider;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class ServiceProviderCorrectionsListPageAction extends FPActionSupport {

    private ServiceProvider provider = new ServiceProvider();

    private ServiceProviderService providerService;

    @NotNull
    @Override
    public String doExecute() throws Exception {

        if (provider == null || provider.isNew()) {
            log.warn("Incorrect service provider id");
            addActionError(getText("orgs.error.service_provider.incorrect_service_provider_id"));
            return REDIRECT_ERROR;
        }

        Stub<ServiceProvider> stub = stub(provider);
        provider = providerService.read(stub);
        if (provider == null) {
            log.warn("Can't get service provider with id {} from DB", stub.getId());
            addActionError(getText("orgs.error.service_provider.cant_get_provider"));
            return REDIRECT_ERROR;
        } else if (provider.isNotActive()) {
            log.warn("Service provider with id {} is disabled", stub.getId());
            addActionError(getText("orgs.error.service_provider.cant_get_provider"));
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

    public ServiceProvider getProvider() {
        return provider;
    }

    public void setProvider(ServiceProvider provider) {
        this.provider = provider;
    }

    @Required
    public void setProviderService(ServiceProviderService providerService) {
        this.providerService = providerService;
    }

}
