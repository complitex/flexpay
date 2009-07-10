package org.flexpay.payments.process.export;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.service.Roles;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.process.ServiceProviderAttribute;
import org.flexpay.payments.service.ServiceProviderAttributeService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratePaymentsRegistry extends QuartzJobBean {

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String USER_PAYMENTS_REGISTRY_GENERATOR = "payments-registry-generator";
    // time out 10 sec
    private static final long TIME_OUT = 10000;

    private Long providerId;
    private Long registeredOrganizationId;
    private String email;
    private String privateKey;

    private ProcessManager processManager;
    private ServiceProviderService serviceProviderService;
    private ServiceProviderAttributeService serviceProviderAttributeService;

    /**
     * Set of authorities names for payments registry
     */
    protected static final List<String> USER_PAYMENTS_REGISTRY_GENERATOR_AUTHORITIES = CollectionUtils.list(
            Roles.BASIC,
            Roles.PROCESS_READ,
            org.flexpay.payments.service.Roles.OPERATION_READ,
            org.flexpay.payments.service.Roles.DOCUMENT_READ,
            org.flexpay.orgs.service.Roles.ORGANIZATION_READ,
            org.flexpay.orgs.service.Roles.SERVICE_PROVIDER_READ,
            org.flexpay.payments.service.Roles.SERVICE_TYPE_READ
    );

    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        log.debug("Starting process generate payments registry at {}", new Date());

        try {
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
                    Date finishDate = DateUtil.now();
                    parameters.put("finishDate", finishDate);
                    parameters.put("OrganizationId", organization.getId());
                    parameters.put("ServiceProviderId", providerId);
                    parameters.put("RegisteredOrganizationId", registeredOrganizationId);
                    //parameters.put("Organization", organization);
                    parameters.put("Email", email);
                    log.debug("set PrivateKey='{}'", privateKey);
                    parameters.put("PrivateKey", privateKey);

                    long processId = processManager.createProcess("GeneratePaymentsRegisryProcess", parameters);
                    Process process;
                    do {
                        process = processManager.getProcessInstanceInfo(processId);
                        try {
                            Thread.sleep(TIME_OUT);
                        } catch (InterruptedException e) {
                            log.warn("Interrupted", e);
                            throw new JobExecutionException(e);
                        }
                        log.debug("Waiting process ({}) will complete ...", processId);
                    } while (!process.getProcessState().isCompleted());

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
                            log.debug("Old last processed date: {}", lastProcessedDateAttribute.getValue());
                            log.debug("New last processed date: {}", lastProcessedDate);
                            lastProcessedDateAttribute.setValue(lastProcessedDate);
                            serviceProviderAttributeService.save(lastProcessedDateAttribute);
                            log.debug("Change last processed date");
                        } else {
                            log.debug("Last processed date did not changed");
                        }

                    }
                    JobDataMap contextParameters = context.getMergedJobDataMap();
                    for (Map.Entry<Serializable, Serializable> param : parameters.entrySet()) {
                        contextParameters.put(param.getKey(), param.getValue());
                    }
                } else {
                    log.error("Organization did not find for service provider with id {}", providerId);
                }
            } else {
                log.error("Service provider by id {} did not find", providerId);
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

    @Required
    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    @Required
    public void setRegisteredOrganizationId(Long registeredOrganizationId) {
        this.registeredOrganizationId = registeredOrganizationId;
    }

    @Required
    public void setEmail(String email) {
        this.email = email;
    }

    @Required
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    @Required
    public void setProviderService(ServiceProviderService providerService) {
        this.serviceProviderService = providerService;
    }

    @Required
    public void setServiceProviderAttributeService(ServiceProviderAttributeService serviceProviderAttributeService) {
        this.serviceProviderAttributeService = serviceProviderAttributeService;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
