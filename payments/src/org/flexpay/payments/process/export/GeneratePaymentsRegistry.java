package org.flexpay.payments.process.export;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.service.Roles;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.process.ServiceProviderAttribute;
import org.flexpay.payments.service.ServiceProviderAttributeService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class GeneratePaymentsRegistry extends QuartzJobBean {

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String USER_PAYMENTS_REGISTRY_GENERATOR = "payments-registry-generator";
    // time out 10 sec
    private static final long TIME_OUT = 10000;
    private static final Integer PAGE_SIZE = 20;

    private Long registeredOrganizationId;
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
    private static final String LAST_PROCESSED_DATE = "lastProcessedDate";

    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        log.debug("Starting process generate payments registry at {}", new Date());

        try {
            authenticatePaymentsRegistryGenerator();

            //ServiceProvider serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(providerId));

            Page<ServiceProvider> page = new Page<ServiceProvider>(PAGE_SIZE);
            List<ServiceProvider> allServiceProviders;
            List<Long> listProcessInstanesId = new ArrayList<Long>();
            while((allServiceProviders = serviceProviderService.listInstances(page)).size() > 0) {
                for (ServiceProvider serviceProvider : allServiceProviders) {
                    Organization organization = serviceProvider.getOrganization();
                    if (organization != null && organization.getId() != null) {
                        //List<ServiceProviderAttribute> attributes = serviceProviderAttributeService.listServiceProviderAttributes(Stub.stub(serviceProvider));
                        Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
                        ServiceProviderAttribute lastProcessedDateAttribute = serviceProviderAttributeService.getServiceProviderAttribute(Stub.stub(serviceProvider), LAST_PROCESSED_DATE);

                        /*
                        if (attributes.size() > 0) {
                            ServiceProviderAttribute lastProcessedDateAttribute = attributes.get(0);
                            parameters.put(lastProcessedDateAttribute.getName(), lastProcessedDateAttribute.getValue());
                        } */
                        if (lastProcessedDateAttribute != null) {
                            parameters.put(lastProcessedDateAttribute.getName(), lastProcessedDateAttribute.getValue());
                        }

                        Date finishDate = DateUtil.now();
                        parameters.put("finishDate", finishDate);
                        parameters.put("OrganizationId", organization.getId());
                        parameters.put("ServiceProviderId", serviceProvider.getId());
                        parameters.put("RegisteredOrganizationId", registeredOrganizationId);
                        parameters.put("Email", serviceProvider.getEmail());
                        parameters.put("PrivateKey", privateKey);


                        long processId = processManager.createProcess("GeneratePaymentsRegisryProcess", parameters);
                        listProcessInstanesId.add(processId);
                    } else {
                        log.error("Organization did not find for service provider with id {}", serviceProvider.getId());
                    }
                }
                page.nextPage();
            }

            do {
                log.debug("Waiting GeneratePaymentsRegisryProcess processes will complete for organization {} ...", registeredOrganizationId);
                try {
                    Thread.sleep(TIME_OUT);
                } catch (InterruptedException e) {
                    log.warn("Interrupted", e);
                    throw new JobExecutionException(e);
                }

                List<Long> tmpListProcessInstanesId = new ArrayList<Long>(listProcessInstanesId);

                for (Long processId : listProcessInstanesId) {
                    Process process;

                    process = processManager.getProcessInstanceInfo(processId);

                    if (process.getProcessState().isCompleted()) {

                        Map<Serializable, Serializable> parameters = process.getParameters();

                        String lastProcessedDate = (String) parameters.get(LAST_PROCESSED_DATE);
                        Long serviceProviderId = (Long) parameters.get("ServiceProviderId");
                        ServiceProvider serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(serviceProviderId));

                        if (lastProcessedDate != null && serviceProvider != null) {
                            ServiceProviderAttribute lastProcessedDateAttribute = serviceProviderAttributeService.getServiceProviderAttribute(Stub.stub(serviceProvider), LAST_PROCESSED_DATE);
                            /*
                            ServiceProviderAttribute lastProcessedDateAttribute = null;
                            if (attributes.size() > 0) {
                                lastProcessedDateAttribute = attributes.get(0);
                            } */

                            if (lastProcessedDateAttribute == null) {
                                lastProcessedDateAttribute = new ServiceProviderAttribute();
                                lastProcessedDateAttribute.setServiceProvider(serviceProvider);
                                lastProcessedDateAttribute.setName(LAST_PROCESSED_DATE);
                                log.debug("Set last processed date: {}", lastProcessedDate);
                                lastProcessedDateAttribute.setValue(lastProcessedDate);

                                serviceProviderAttributeService.save(lastProcessedDateAttribute);
                                log.debug("Last processed date was null");
                            } else if (!lastProcessedDate.equals(lastProcessedDateAttribute.getValue())) {
                                log.debug("Old last processed date: {}", lastProcessedDateAttribute.getValue());
                                log.debug("New last processed date: {}", lastProcessedDate);
                                lastProcessedDateAttribute.setValue(lastProcessedDate);

                                serviceProviderAttributeService.save(lastProcessedDateAttribute);
                                log.debug("Change last processed date");
                            } else {
                                log.debug("Last processed date did not changed");
                            }

                        }
                        
                        tmpListProcessInstanesId.remove(processId);
                        /*
                                        JobDataMap contextParameters = context.getMergedJobDataMap();
                                        for (Map.Entry<Serializable, Serializable> param : parameters.entrySet()) {
                                            contextParameters.put(param.getKey(), param.getValue());
                                        } */
                    }
                }
                listProcessInstanesId = tmpListProcessInstanesId;
            } while (listProcessInstanesId.size() > 0);
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
    public void setRegisteredOrganizationId(Long registeredOrganizationId) {
        this.registeredOrganizationId = registeredOrganizationId;
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
