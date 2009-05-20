package org.flexpay.payments.process.export;

import org.springframework.scheduling.quartz.QuartzJobBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.flexpay.common.process.*;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.service.Roles;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.service.ServiceProviderAttributeService;
import org.flexpay.payments.persistence.process.ServiceProviderAttribute;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

public class GeneratePaymentsRegistry extends QuartzJobBean {
    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String USER_PAYMENTS_REGISTRY_GENERATOR = "payments-registry-generator";

    private ProcessManager processManager;
    private ServiceProviderService serviceProviderService;
    private ServiceProviderAttributeService serviceProviderAttributeService;

    private Long providerId;

    /**
	 * Set of authorities names for payments registry
	 */
	protected static final List<String> USER_PAYMENTS_REGISTRY_GENERATOR_AUTHORITIES = CollectionUtils.list(
			Roles.BASIC,
            Roles.PROCESS_READ,
            org.flexpay.payments.service.Roles.OPERATION_READ,
            org.flexpay.orgs.service.Roles.ORGANIZATION_READ,
            org.flexpay.orgs.service.Roles.SERVICE_PROVIDER_READ
	);

    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if (log.isDebugEnabled()) {
            log.debug("Starting process generate payments registry at {}", new Date());
        }
        try {
            // Map<Serializable, Serializable> parameters = context.getMergedJobDataMap().getWrappedMap();
            authenticatePaymentsRegistryGenerator();

            ServiceProvider serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(providerId));
            if (serviceProvider != null) {
                Organization organization = serviceProvider.getOrganization();
                if (organization != null && organization.getId() != null) {
                    List<ServiceProviderAttribute> attributes = serviceProviderAttributeService.listServiceProviderAttributes(Stub.stub(serviceProvider));
                    Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
                    ServiceProviderAttribute lastProcessedDateAttribute = null;
                    for (ServiceProviderAttribute attribute : attributes) {
                        if ("lastProcessedDate".equals(attribute.getName())) {
                            lastProcessedDateAttribute = attribute;
                            parameters.put(attribute.getName(), attribute.getValue());
                        }
                    }
                    //parameters.put("OrganizationId", organization.getId());
                    parameters.put("Organization", organization);
                    long processId = processManager.createProcess("GeneratePaymentsRegisryProcess", parameters);
                    Process process = processManager.getProcessInstanceInfo(processId);
                    parameters = process.getParameters();
                    String lastProcessedDate = (String) parameters.get("lastProcessedDate");
                    if (lastProcessedDate != null) {
                        if (lastProcessedDateAttribute == null) {
                            lastProcessedDateAttribute = new ServiceProviderAttribute();
                            lastProcessedDateAttribute.setServiceProvider(serviceProvider);
                            lastProcessedDateAttribute.setName("lastProcessedDate");
                            log.debug("Last processed date is null");
                        }
                        if (!lastProcessedDate.equals(lastProcessedDateAttribute.getValue())) {
                            log.debug("Old last processed date: " + lastProcessedDateAttribute.getValue());
                            log.debug("New last processed date: " + lastProcessedDate);
                            lastProcessedDateAttribute.setValue(lastProcessedDate);
                            serviceProviderAttributeService.save(lastProcessedDateAttribute);
                            log.debug("Change last processed date");
                        } else {
                            log.debug("Last processed date did not changed");
                        }
                    }
                } else {
                    log.error("Organization did not find for service provider with id '" + providerId + "'");
                }
            } else {
                log.error("Service provider by id '" + providerId + "' did not find");
            }
        } catch (ProcessInstanceException e) {
            log.error("Failed run process generate payments registry", e);
            throw new JobExecutionException(e);
        } catch (ProcessDefinitionException e) {
            log.error("Process generate payments registry not started", e);
            throw new JobExecutionException(e);
        }
    }

    /**
	 * Do payments registry user authentication
	 */
	private static void authenticatePaymentsRegistryGenerator() {
		SecurityUtil.authenticate(USER_PAYMENTS_REGISTRY_GENERATOR, USER_PAYMENTS_REGISTRY_GENERATOR_AUTHORITIES);
	}

    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    public void setProviderService(ServiceProviderService providerService) {
        this.serviceProviderService = providerService;
    }

    public void setServiceProviderAttributeService(ServiceProviderAttributeService serviceProviderAttributeService) {
        this.serviceProviderAttributeService = serviceProviderAttributeService;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }
}
