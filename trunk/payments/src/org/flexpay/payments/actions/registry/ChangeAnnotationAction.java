package org.flexpay.payments.actions.registry;

import org.flexpay.payments.actions.CashboxCookieActionSupport;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryContainer;
import org.flexpay.common.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class ChangeAnnotationAction extends CashboxCookieActionSupport {
    /**
     * Symbol used escape special symbols
     */
    private static final char ESCAPE_SYMBOL = '\\';

    /**
     * Symbol used to split fields in containers
     */
    private static final char CONTAINER_DATA_DELIMITER = ':';

    private static final String ANNOTATION_CONTAINER_TYPE = "1001";

    private static final long CONTAINER_DATA_MAX_SIZE = 2048;

    private static final String CHARSET = "UTF-8";

    private Registry registry = new Registry();
    private String registryAnnotation;
    private String submitChange;
    private String cancel;

    private RegistryService registryService;

    @NotNull
    protected String doExecute() throws Exception {
        if (!StringUtils.isEmpty(cancel)) {
            log.debug("Canceled edit annotation");
            return NONE;
        }
        if (registry.getId() == null) {
            log.error("No registryId specified, give up");
			addActionError(getText("payments.registry.not_specified"));
			return ERROR;
		}
        Long currentRegistryId = registry.getId();
        registry = registryService.readWithContainers(Stub.stub(registry));
        if (registry == null) {
            log.error("Missing registryId={}. Registry does not find.", currentRegistryId);
            addActionError(getText("payments.registry.not_found", new String[] {String.valueOf(currentRegistryId)}));
            return ERROR;
        }
        // TODO Make factory
        List<RegistryContainer> containers = registry.getContainers();
        RegistryContainer annotationContainer = null;
        String annotaion = null;
        for (RegistryContainer registryContainer : containers) {
            List<String> containerData = StringUtil.splitEscapable(
                    registryContainer.getData(), CONTAINER_DATA_DELIMITER, ESCAPE_SYMBOL);
            if (containerData != null && containerData.size() > 0 && ANNOTATION_CONTAINER_TYPE.equals(containerData.get(0))) {
                annotationContainer = registryContainer;
                if (containerData.size() > 1) {
                    annotaion = new String(Base64.decodeBase64(containerData.get(1).getBytes()), CHARSET);
                }
                break;
            }
        }
        if (!StringUtils.isEmpty(submitChange) && !StringUtils.isEmpty(registryAnnotation)) {
            String encodeRegistryAnnotation = new String(Base64.encodeBase64(registryAnnotation.getBytes(CHARSET)));
            String annotationContainerData = ANNOTATION_CONTAINER_TYPE + CONTAINER_DATA_DELIMITER + encodeRegistryAnnotation;
            if (annotationContainerData.length() > CONTAINER_DATA_MAX_SIZE) {
                long maxSize = CONTAINER_DATA_MAX_SIZE - registryAnnotation.length() + encodeRegistryAnnotation.length();
                addActionError(getText("payments.registry.annotation.max_size", new String[] {String.valueOf(maxSize)}));
                return ERROR;
            }
            if (annotationContainer == null) {
                annotationContainer = new RegistryContainer();
                annotationContainer.setRegistry(registry);
                containers.add(annotationContainer);
            }
            annotationContainer.setData(annotationContainerData);
            registryService.update(registry);

            log.debug("Annotation updated for registry {}", registry.getId());

            addActionMessage("payments.registry.annotation.updated");
            return REDIRECT_SUCCESS;

        } else if (!StringUtils.isEmpty(submitChange) && StringUtils.isEmpty(registryAnnotation) && annotationContainer != null) {
            annotationContainer.setData("");
            registryService.update(registry);

            log.debug("Annotation remove for registry {}", registry.getId());

            addActionMessage("payments.registry.annotation.deleted");
            return REDIRECT_SUCCESS;
            
        } else if (annotaion != null) {
            registryAnnotation = annotaion;
        }

        return SUCCESS;
    }

    @NotNull
    protected String getErrorResult() {
        return ERROR;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public String getRegistryAnnotation() {
        return registryAnnotation;
    }

    public void setRegistryAnnotation(String registryAnnotation) {
        this.registryAnnotation = registryAnnotation;
    }

    public String getSubmitChange() {
        return submitChange;
    }

    public void setSubmitChange(String submitChange) {
        this.submitChange = submitChange;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    @Required
    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }
}
