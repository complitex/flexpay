package org.flexpay.payments.process.export.job;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.RegistryService;
import org.flexpay.payments.util.registries.RegistryFPFileFormat;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.*;

/**
 * Job generate payments registry in FP format.
 */
public class GeneratePaymentsFPRegistryJob extends Job {

	private RegistryService registryService;

	private RegistryFPFileFormat exportPaymentsRegistry;

    /**
     * Create new file in FP format from db registry and attach result to it.<br/>
     * Job parameters map must content {@link org.flexpay.payments.process.export.ExportJobParameterNames#REGISTRY_ID} (type falue is {@link Long}) or
     * {@link org.flexpay.payments.process.export.ExportJobParameterNames#REGISTRY} (type value is {@link Registry})
     *
     * @param parameters Job parameters map
     * @return {@link #RESULT_NEXT} if file generated successfully.<br/>{@link #RESULT_ERROR} if registry did not find in job parameters map or generation had errors.
     * @throws FlexPayException
     */
    public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		Registry registry = getRegistry(parameters);
        if (registry == null) {
            return RESULT_ERROR;
        }

        log.debug("Run generate FP file of registry {}", registry.getId());

        FPFile file = exportPaymentsRegistry.generateAndAttachFile(registry, getPrivateKey(parameters));
        if (file == null) {
            log.error("FP file does not generate for registry {}", registry.getId());
            return RESULT_ERROR;
        }

        log.debug("Success generate FP file of registry {}", registry.getId());

        return RESULT_NEXT;
    }

    private Registry getRegistry(Map<Serializable, Serializable> parameters) {

		Registry registry = null;

		if (parameters.containsKey(REGISTRY)) {
			Object o = parameters.get(REGISTRY);
			if (o instanceof Registry) {
				registry = (Registry) o;
			} else {
				log.error("Invalid registry`s instance class");
			}
		} else if (parameters.containsKey(REGISTRY_ID)) {
			Long registryId = (Long) parameters.get(REGISTRY_ID);
			registry = registryService.read(new Stub<Registry>(registryId));
		}

		return registry;
	}

	private String getPrivateKey(Map<Serializable, Serializable> parameters) {

		if (parameters.containsKey(PRIVATE_KEY)) {
            return (String) parameters.get(PRIVATE_KEY);
		}

		return null;
	}

    @Required
    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Required
    public void setExportPaymentsRegistry(RegistryFPFileFormat exportPaymentsRegistry) {
        this.exportPaymentsRegistry = exportPaymentsRegistry;
    }
}
