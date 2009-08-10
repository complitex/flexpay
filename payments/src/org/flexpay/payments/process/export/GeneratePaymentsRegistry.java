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
import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.orgs.service.PaymentsCollectorService;
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

    private String privateKey;

    private ProcessManager processManager;
    private ServiceProviderService serviceProviderService;
    private ServiceProviderAttributeService serviceProviderAttributeService;
    private PaymentsCollectorService paymentsCollectorService;

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
            org.flexpay.payments.service.Roles.SERVICE_TYPE_READ,
            org.flexpay.orgs.service.Roles.PAYMENTS_COLLECTOR_READ
    );
    private static final String LAST_PROCESSED_DATE = "lastProcessedDate";

    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        log.debug("Starting process generate payments registry at {}", new Date());

        try {
            authenticatePaymentsRegistryGenerator();

            Page<PaymentsCollector> paymentsCollectorsPage = new Page<PaymentsCollector>(PAGE_SIZE);
            List<PaymentsCollector> listPaymentsCollectors;

            Page<ServiceProvider> serviceProvidersPage = new Page<ServiceProvider>(PAGE_SIZE);
            List<ServiceProvider> listServiceProviders;
            List<Long> listProcessInstanesId = new ArrayList<Long>();
            List<Map<Serializable, Serializable>> waitingProcessData = new ArrayList<Map<Serializable, Serializable>>();

            while((listPaymentsCollectors = paymentsCollectorService.listInstances(paymentsCollectorsPage)).size() > 0) {
                for (PaymentsCollector paymentsCollector : listPaymentsCollectors) {
                    log.debug("Payment collector (registered) organization {}", paymentsCollector.getOrganization().getId());

                    serviceProvidersPage.setPageNumber(0);
                    while((listServiceProviders = serviceProviderService.listInstances(serviceProvidersPage)).size() > 0) {
                        for (ServiceProvider serviceProvider : listServiceProviders) {
                            Organization organization = serviceProvider.getOrganization();
                            if (organization != null && organization.getId() != null) {
                                Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
                                ServiceProviderAttribute lastProcessedDateAttribute = serviceProviderAttributeService.getServiceProviderAttribute(Stub.stub(serviceProvider), LAST_PROCESSED_DATE);

                                if (lastProcessedDateAttribute != null) {
                                    parameters.put(lastProcessedDateAttribute.getName(), lastProcessedDateAttribute.getValue());
                                }

                                Date finishDate = DateUtil.now();
                                parameters.put("finishDate", finishDate);
                                parameters.put("OrganizationId", organization.getId());
                                parameters.put("ServiceProviderId", serviceProvider.getId());
                                parameters.put("RegisteredOrganizationId", paymentsCollector.getOrganization().getId());
                                parameters.put("Email", serviceProvider.getEmail());
                                parameters.put("PrivateKey", privateKey);

                                waitingProcessData.add(parameters);

                                long processId = processManager.createProcess("GeneratePaymentsRegisryProcess", parameters);
                                listProcessInstanesId.add(processId);
                            } else {
                                log.error("Organization did not find for service provider with id {}", serviceProvider.getId());
                            }
                        }
                        serviceProvidersPage.nextPage();
                    }
                }
                paymentsCollectorsPage.nextPage();
            }

            do {
                for (Map<Serializable, Serializable> param : waitingProcessData) {
                    log.debug("Waiting GeneratePaymentsRegisryProcess processes will complete for: {} ...", param);
                }
                try {
                    Thread.sleep(TIME_OUT);
                } catch (InterruptedException e) {
                    log.warn("Interrupted", e);
                    throw new JobExecutionException(e);
                }

                List<Long> tmpListProcessInstanesId = new ArrayList<Long>(listProcessInstanesId);
                waitingProcessData = new ArrayList<Map<Serializable, Serializable>>();

                for (Long processId : listProcessInstanesId) {
                    Process process;

                    process = processManager.getProcessInstanceInfo(processId);

                    if (process.getProcessState().isCompleted()) {
                        log.debug("Process {} completed: {} ", processId, process.getParameters());

                        Map<Serializable, Serializable> parameters = process.getParameters();

                        String lastProcessedDate = (String) parameters.get(LAST_PROCESSED_DATE);
                        Long serviceProviderId = (Long) parameters.get("ServiceProviderId");
                        ServiceProvider serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(serviceProviderId));

                        if (lastProcessedDate != null && serviceProvider != null) {
                            ServiceProviderAttribute lastProcessedDateAttribute = serviceProviderAttributeService.getServiceProviderAttribute(Stub.stub(serviceProvider), LAST_PROCESSED_DATE);

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
                    } else {
                        log.debug("Process {} has status {}", processId, process.getProcessState());
                        waitingProcessData.add(process.getParameters());
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

    @Required
    public void setPaymentsCollectorService(PaymentsCollectorService paymentsCollectorService) {
        this.paymentsCollectorService = paymentsCollectorService;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
