package org.flexpay.payments.process.export.handler2;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.RegistryService;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.RegistryDeliveryHistory;
import org.flexpay.payments.service.RegistryDeliveryHistoryService;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.*;

public class SendPaymentsRegistryHandler extends SendFileHandler {

	// required services
	private RegistryService registryService;
    private ServiceProviderService providerService;
    private RegistryDeliveryHistoryService registryDeliveryHistoryService;

    @Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {

        Registry registry = getRegistry(parameters);
        if (registry == null) {
            log.error("Registry was not found as a job parameter");
            return RESULT_ERROR;
        }

        if (registry.getProperties() == null || !(registry.getProperties() instanceof EircRegistryProperties)) {
            log.error("Registry {} doesn`t contain properties", registry.getId());
            return RESULT_ERROR;
        }

        EircRegistryProperties properties = (EircRegistryProperties)registry.getProperties();
        ServiceProvider serviceProvider = providerService.read(properties.getServiceProviderStub());
        if (serviceProvider == null) {
            log.error("Registry {} properties don`t content service provider", registry.getId());
            return RESULT_ERROR;
        }
        if (StringUtils.isEmpty(serviceProvider.getEmail())) {
            log.error("Service provider {} of registry {} does not have an e-mail",
					new Object[]{serviceProvider.getId(), registry.getId()});
            return RESULT_ERROR;
        }
        String email = serviceProvider.getEmail();
		parameters.put(EMAIL, email);

        String result = super.execute(parameters);
		if (RESULT_NEXT.equals(result)) {
            addToDeliveryHistory(getFile(parameters), registry, serviceProvider);
        }
        return RESULT_NEXT;
	}

	private void addToDeliveryHistory(FPFile file, Registry registry, ServiceProvider serviceProvider) {

		RegistryDeliveryHistory registryDeliveryHistory = new RegistryDeliveryHistory();
		registryDeliveryHistory.setDeliveryDate(new Date());
		registryDeliveryHistory.setEmail(serviceProvider.getEmail());
		registryDeliveryHistory.setSpFile(file);
		registryDeliveryHistory.setRegistry(registry);
		registryDeliveryHistoryService.create(registryDeliveryHistory);
	}

	private Registry getRegistry(Map<String, Object> parameters) {

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

    @Required
    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Required
    public void setProviderService(ServiceProviderService providerService) {
        this.providerService = providerService;
    }

    @Required
    public void setRegistryDeliveryHistoryService(RegistryDeliveryHistoryService registryDeliveryHistoryService) {
        this.registryDeliveryHistoryService = registryDeliveryHistoryService;
    }
}
