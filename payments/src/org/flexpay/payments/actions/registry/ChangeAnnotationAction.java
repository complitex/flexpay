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

    private String registryId;
    private String registryAnnotation;
    private String submitChange;
    private String cancel;

    private RegistryService registryService;

    @NotNull
    protected String doExecute() throws Exception {
        if (cancel != null && cancel.length() > 0) {
            return NONE;
        }
        if (registryId == null) {
            log.error("Registry does not set");
            return ERROR;
        }
        Long id;
        try {
            id = Long.parseLong(registryId);
        } catch (NumberFormatException e) {
            log.error("Missing registryId={}", registryId, e);
            return ERROR;
        }
        Registry registry = registryService.readWithContainers(new Stub<Registry>(id));
        if (registry == null) {
            log.error("Missing registryId={}. Registry does not find.", registryId);
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
                    annotaion = containerData.get(1);
                }
                break;
            }
        }
        if (submitChange != null && submitChange.length() > 0) {
            if (registryAnnotation != null && registryAnnotation.length() > 0) {
                String encodeRegistryAnnotation = new String(Base64.encodeBase64(registryAnnotation.getBytes()));
                String annotationContainerData = ANNOTATION_CONTAINER_TYPE + CONTAINER_DATA_DELIMITER + encodeRegistryAnnotation;
                if (annotationContainerData.length() > CONTAINER_DATA_MAX_SIZE) {
                    long maxSize = CONTAINER_DATA_MAX_SIZE + registryAnnotation.length() - encodeRegistryAnnotation.length();
                    addActionError(getText("payments.registry.annotation.max_size", String.valueOf(maxSize)));
                    return ERROR;
                }
                if (annotationContainer == null) {
                    annotationContainer = new RegistryContainer();
                    annotationContainer.setRegistry(registry);
                    containers.add(annotationContainer);
                }
                annotationContainer.setData(annotationContainerData);
                registryService.update(registry);

                return REDIRECT_SUCCESS;
            } else {
                if (annotationContainer != null) {
                    containers.remove(annotationContainer);
                    registryService.update(registry);

                    return REDIRECT_SUCCESS;
                }
            }
        } else if (annotaion != null) {
            registryAnnotation = annotaion;
        }

        return SUCCESS;
    }

    @NotNull
    protected String getErrorResult() {
        return ERROR;
    }

    public String getRegistryId() {
        return registryId;
    }

    public void setRegistryId(String registryId) {
        this.registryId = registryId;
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
