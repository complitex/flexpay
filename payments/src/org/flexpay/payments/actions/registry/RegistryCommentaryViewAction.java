package org.flexpay.payments.actions.registry;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryContainer;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.StringUtil;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.persistence.registry.RegistryContainer.*;

public class RegistryCommentaryViewAction extends AccountantAWPActionSupport {

    private Registry registry = new Registry();
    private String paymentNumber;
    private String paymentDate;
    private String commentary;

    private RegistryService registryService;

    @NotNull
    @Override
    protected String doExecute() throws Exception {

        if (registry == null || registry.getId() == null) {
            log.warn("Incorrect registry id");
			addActionError(getText("payments.registry.not_specified"));
			return REDIRECT_ERROR;
		}

        Stub<Registry> stub = stub(registry);
        registry = registryService.readWithContainers(stub);
        if (registry == null) {
            log.warn("Can't get registry with id {} from DB", stub.getId());
            addActionError(getText("payments.registry.not_found", new String[] {String.valueOf(stub.getId())}));
            return REDIRECT_ERROR;
        }

        List<RegistryContainer> containers = registry.getContainers();
        boolean created = false;
        for (RegistryContainer registryContainer : containers) {
            List<String> containerData = StringUtil.splitEscapable(
                    registryContainer.getData(), CONTAINER_DATA_DELIMITER, ESCAPE_SYMBOL);
            if (containerData != null && !containerData.isEmpty() && COMMENTARY_CONTAINER_TYPE.equals(containerData.get(0))) {
                created = true;
                if (containerData.size() > 3) {
                    paymentNumber = containerData.get(1);
                    paymentDate = containerData.get(2);
                    commentary = containerData.get(3);
                }
                break;
            }
        }

        if (!created) {
            log.debug("Commentary for registry with id {} not found", registry.getId());
            return REDIRECT_ERROR;
        }

        return SUCCESS;
    }

    @NotNull
    @Override
    protected String getErrorResult() {
        return REDIRECT_ERROR;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getCommentary() {
        return commentary;
    }

    @Required
    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }
}
