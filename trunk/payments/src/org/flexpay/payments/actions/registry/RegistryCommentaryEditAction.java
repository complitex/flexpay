package org.flexpay.payments.actions.registry;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryContainer;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.StringUtil;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.persistence.registry.RegistryContainer.*;

public class RegistryCommentaryEditAction extends AccountantAWPActionSupport {

    public static final SimpleDateFormat format = new SimpleDateFormat(COMMENTARY_PAYMENT_DATE_FORMAT);

    private Registry registry = new Registry();
    private String commentary;
    private String paymentNumber = "";
    private String paymentDate = "";

    private RegistryService registryService;

    @NotNull
    @Override
    protected String doExecute() throws Exception {

        if (registry == null || registry.getId() == null) {
            log.warn("Incorrect registry id");
			addActionError(getText("payments.registry.not_specified"));
			return REDIRECT_SUCCESS;
		}

        Stub<Registry> stub = stub(registry);
        registry = registryService.readWithContainers(stub);
        if (registry == null) {
            log.warn("Can't get registry with id {} from DB", stub.getId());
            addActionError(getText("payments.registry.not_found", new String[] {String.valueOf(stub.getId())}));
            return REDIRECT_SUCCESS;
        }

        // TODO Make factory
        List<RegistryContainer> containers = registry.getContainers();
        RegistryContainer commentaryContainer = null;
        String containerCommentary = null;
        String containerPaymentNumber = null;
        String containerPaymentDate = null;
        for (RegistryContainer registryContainer : containers) {
            List<String> containerData = StringUtil.splitEscapable(
                    registryContainer.getData(), CONTAINER_DATA_DELIMITER, ESCAPE_SYMBOL);
            if (containerData != null && !containerData.isEmpty() && COMMENTARY_CONTAINER_TYPE.equals(containerData.get(0))) {
                commentaryContainer = registryContainer;
                if (containerData.size() > 3) {
                    containerPaymentNumber = containerData.get(1);
                    containerPaymentDate = containerData.get(2);
                    containerCommentary = containerData.get(3);
                }
                break;
            }
        }

        if (containerPaymentDate == null) {
            containerPaymentDate = format.format(new Date());
        }

        if (isSubmit()) {

            if (!doValidate()) {
                return INPUT;
            }

            if (!StringUtils.isEmpty(commentary)) {

                String commentaryContainerData = COMMENTARY_CONTAINER_TYPE + CONTAINER_DATA_DELIMITER +
                        paymentNumber + CONTAINER_DATA_DELIMITER +
                        paymentDate + CONTAINER_DATA_DELIMITER +
                        commentary;

                if (commentaryContainerData.length() > CONTAINER_DATA_MAX_SIZE) {
                    addActionError(getText("payments.error.registry.commentary.commentary_too_long"));
                    return INPUT;
                }
                if (commentaryContainer == null) {
                    commentaryContainer = new RegistryContainer();
                    commentaryContainer.setRegistry(registry);
                    containers.add(commentaryContainer);
                }
                commentaryContainer.setData(commentaryContainerData);
                registryService.update(registry);

                log.debug("Registry commentary updated for registry {}", registry.getId());
                addActionMessage(getText("payments.registry.commentary.updated"));

            } else {

                if (commentaryContainer != null) {
                    commentaryContainer.setData("");
                }

                registryService.update(registry);

                log.debug("Registry commentary remove for registry {}", registry.getId());
                addActionMessage(getText("payments.registry.commentary.deleted"));

            }

            return REDIRECT_SUCCESS;

        }

        commentary = containerCommentary;
        paymentNumber = containerPaymentNumber;
        paymentDate = containerPaymentDate;

        return INPUT;
    }

    private boolean doValidate() {

        if (paymentNumber != null && paymentNumber.length() > COMMENTARY_PAYMENT_NUMBER_LENGTH) {
            addActionError("payments.error.registry.commentary.payment_number_too_long");
        }

        if (commentary != null && commentary.length() > COMMENTARY_DATA_LENGTH) {
            addActionError("payments.error.registry.commentary.commentary_too_long");
        }

        return !hasActionErrors();
    }

    @NotNull
    @Override
    protected String getErrorResult() {
        return INPUT;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Required
    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }
}
