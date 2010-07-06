package org.flexpay.payments.actions.registry;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.RegistryService;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class CheckRegistryErrorsNumberAction extends AccountantAWPActionSupport {

    private Registry registry = new Registry();

    private RegistryService registryService;

    @NotNull
    @Override
    public String doExecute() throws Exception {

        if (registry == null || registry.isNew()) {
            log.warn("Incorrect registry id");
            addActionError(getText("payments.error.registry.incorrect_registry_id"));
            return SUCCESS;
        }

        Stub<Registry> stub = stub(registry);
        registry = registryService.read(stub);
        if (registry == null) {
            log.warn("Can't get registry with id {} from DB", stub.getId());
            addActionError(getText("payments.error.registry.cant_get_registry"));
            return SUCCESS;
        }

        StopWatch watch = new StopWatch();
        if (log.isDebugEnabled()) {
            watch.start();
        }

        registryService.checkRegistryErrorsNumber(registry);

        if (log.isDebugEnabled()) {
            watch.stop();
            log.debug("Prior listing actions took: {}", watch);
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

    @Required
    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

}
