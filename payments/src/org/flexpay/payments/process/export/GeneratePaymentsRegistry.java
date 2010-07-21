package org.flexpay.payments.process.export;

import org.flexpay.common.dao.paging.FetchRange;
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
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.ServiceProviderAttribute;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.orgs.service.ServiceProviderAttributeService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.process.export.job.ExportJobParameterNames;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.payments.process.export.job.ExportJobParameterNames.*;

/**
 * Scheduling job generate payments registries for all service providers and registered organizations.
 */
public class GeneratePaymentsRegistry extends QuartzJobBean {

	private Logger log = LoggerFactory.getLogger(getClass());

	private static final String USER_PAYMENTS_REGISTRY_GENERATOR = "payments-registry-generator";
    private static final String GENERATE_PAYMENTS_REGISRY_PROCESS = "GeneratePaymentsRegisryProcess";
    
	// time out 10 sec
	private static final long TIME_OUT = 10000;

	private String privateKey;

	private ProcessManager processManager;
	private ServiceProviderService serviceProviderService;
	private ServiceProviderAttributeService serviceProviderAttributeService;
	private PaymentCollectorService paymentCollectorService;

	/**
	 * Set of authorities names for payments registry
	 */
	protected static final List<String> USER_PAYMENTS_REGISTRY_GENERATOR_AUTHORITIES = list(
			Roles.BASIC,
			Roles.PROCESS_READ,
			org.flexpay.payments.service.Roles.OPERATION_READ,
			org.flexpay.payments.service.Roles.DOCUMENT_READ,
			org.flexpay.orgs.service.Roles.ORGANIZATION_READ,
			org.flexpay.orgs.service.Roles.SERVICE_PROVIDER_READ,
			org.flexpay.payments.service.Roles.SERVICE_TYPE_READ,
			org.flexpay.payments.service.Roles.SERVICE_READ,
			org.flexpay.orgs.service.Roles.PAYMENT_POINT_READ,
			org.flexpay.orgs.service.Roles.PAYMENT_COLLECTOR_READ
	);

	/**
	 * Start processes "GeneratePaymentsRegisryProcess" for all existed in database service providers and registered
	 * organization.<br/> Job wait while all started processes will finish and add generated registeries ids to job
	 * execution context.<br/> Registries ids content in {@link org.quartz.JobExecutionContext#getMergedJobDataMap()}.
	 * Mapping key is {@link ExportJobParameterNames#REGISTRIES} and value`s type is {@link java.util.List}<{@link Long}>
	 *
	 * @param context Job execution context.
	 * @throws JobExecutionException
	 */
    @Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		log.debug("Starting process generate payments registry at {}", new Date());

		try {
			authenticatePaymentsRegistryGenerator();

            FetchRange paymentCollectorRange = new FetchRange();
			List<Long> processInstanceIds = list();

			do {
                for (PaymentCollector paymentCollector : paymentCollectorService.listInstances(paymentCollectorRange)) {
                    Organization paymentCollectorOrganization = paymentCollector.getOrganization();

					log.debug("Payment collector (registered) organization {}", paymentCollectorOrganization.getId());

                    FetchRange serviceProviderRange = new FetchRange();
					do {
                        for (ServiceProvider serviceProvider : serviceProviderService.listInstances(serviceProviderRange)) {
                            long processId = createGeneratePaymentRegistryProcess(paymentCollectorOrganization, serviceProvider);
							processInstanceIds.add(processId);
						}
						serviceProviderRange.nextPage();
					} while (serviceProviderRange.hasMore());
				}
				paymentCollectorRange.nextPage();
			} while (paymentCollectorRange.hasMore());

            List<Long> registryIds = waitCompletionProcesses(processInstanceIds);
			context.getMergedJobDataMap().put(REGISTRIES, registryIds);
		} catch (ProcessInstanceException e) {
			log.error("Failed run process generate payments registry", e);
			throw new JobExecutionException(e);
		} catch (ProcessDefinitionException e) {
			log.error("Process generate payments registry not started", e);
			throw new JobExecutionException(e);
		}
	}

    private List<Long> waitCompletionProcesses(List<Long> processInstanceIds) throws JobExecutionException {
        List<Long> registryIds = list();

        do {
            log.debug("Waiting number {} processes: {}", GENERATE_PAYMENTS_REGISRY_PROCESS, processInstanceIds.size());
            try {
                Thread.sleep(TIME_OUT);
            } catch (InterruptedException e) {
                log.warn("Interrupted", e);
                throw new JobExecutionException(e);
            }

            List<Long> tmpListProcessInstanesId = list(processInstanceIds);

            for (Long processId : processInstanceIds) {
                Process process;

                process = processManager.getProcessInstanceInfo(processId);

                if (process != null && process.getProcessState().isCompleted()) {
                    log.debug("Process {} completed: {} ", processId, process.getParameters());

                    Long registryId = afterCompletedProcess(process);

                    if (registryId != null) {
                        registryIds.add(registryId);
                    }
                    tmpListProcessInstanesId.remove(processId);
                } else if (process == null) {
                    log.debug("Process {} did not find", processId);
                    tmpListProcessInstanesId.remove(processId);
                } else {
                    log.debug("Process {} has status {}: {}", new Object[]{processId, process.getProcessState(), process.getParameters()});
                }
            }
            processInstanceIds = tmpListProcessInstanesId;
        } while (!processInstanceIds.isEmpty());

        return registryIds;
    }

    private Long afterCompletedProcess(Process process) {
        Map<Serializable, Serializable> parameters = process.getParameters();

        String lastProcessedDate = (String) parameters.get(LAST_PROCESSED_DATE);
        Long serviceProviderId = (Long) parameters.get(SERVICE_PROVIDER_ID);
        ServiceProvider serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(serviceProviderId));

        if (lastProcessedDate != null && serviceProvider != null) {
            ServiceProviderAttribute lastProcessedDateAttribute = serviceProviderAttributeService
                    .getServiceProviderAttribute(stub(serviceProvider), LAST_PROCESSED_DATE);

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

        return parameters.containsKey(ExportJobParameterNames.REGISTRY_ID)? (Long) parameters.get(ExportJobParameterNames.REGISTRY_ID) : null;
    }

    private long createGeneratePaymentRegistryProcess(Organization paymentCollectorOrganization, ServiceProvider serviceProvider) throws ProcessInstanceException, ProcessDefinitionException {
        Organization serviceProviderOrganization = serviceProvider.getOrganization();

        Map<Serializable, Serializable> parameters = CollectionUtils.map();
        ServiceProviderAttribute lastProcessedDateAttribute = serviceProviderAttributeService
                .getServiceProviderAttribute(stub(serviceProvider), LAST_PROCESSED_DATE);

        if (lastProcessedDateAttribute != null) {
            parameters.put(lastProcessedDateAttribute.getName(), lastProcessedDateAttribute.getValue());
        }

        Date finishDate = DateUtil.now();
        parameters.put(FINISH_DATE, finishDate);
        parameters.put(ORGANIZATION_ID, serviceProviderOrganization.getId());
        parameters.put(SERVICE_PROVIDER_ID, serviceProvider.getId());
        parameters.put(REGISTERED_ORGANIZATION_ID, paymentCollectorOrganization.getId());
        //parameters.put(ExportJobParameterNames.EMAIL, serviceProvider.getEmail());
        parameters.put(PRIVATE_KEY, privateKey);

        log.debug("start process {}", parameters);
        return processManager.createProcess(GENERATE_PAYMENTS_REGISRY_PROCESS, parameters);
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
	public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
		this.paymentCollectorService = paymentCollectorService;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
}
