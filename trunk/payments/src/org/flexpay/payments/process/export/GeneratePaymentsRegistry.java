package org.flexpay.payments.process.export;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.persistence.ProcessInstance;
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
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.payments.process.export.ExportJobParameterNames.*;

/**
 * Scheduling job generate payments registries for all service providers and registered organizations.
 */
public class GeneratePaymentsRegistry extends QuartzJobBean {

	private Logger log = LoggerFactory.getLogger(getClass());

	private static final String USER_PAYMENTS_REGISTRY_GENERATOR = "payments-registry-generator";
    private static final String GENERATE_PAYMENTS_REGISTRY_PROCESS = "GeneratePaymentsRegistry";
    
	// time out 10 sec
	private static final long TIME_OUT = 2000;

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
			Roles.PROCESS_START,
			Roles.PROCESS_DEFINITION_READ,
			Roles.PROCESS_DEFINITION_UPLOAD_NEW,
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
                        for (ServiceProvider serviceProvider : getServiceProviders(serviceProviderRange)) {
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
			log.error("ProcessInstance generate payments registry not started", e);
			throw new JobExecutionException(e);
		}
	}

	@Transactional (readOnly = false, propagation = Propagation.REQUIRED)
    public List<Long> waitCompletionProcesses(List<Long> processInstanceIds) throws JobExecutionException {
        List<Long> registryIds = list();

        do {
            log.debug("Waiting number {} processes: {}", GENERATE_PAYMENTS_REGISTRY_PROCESS, processInstanceIds.size());
			try {
                Thread.sleep(TIME_OUT);
            } catch (InterruptedException e) {
                log.warn("Interrupted", e);
                throw new JobExecutionException(e);
            }

            List<Long> tmpListProcessInstanesId = list(processInstanceIds);

            for (Long processId : processInstanceIds) {
                ProcessInstance process;

				process = getWaitingProcess(processId);
				//process = new ProcessInstance();

                if (process != null && process.hasEnded()) {
                    log.debug("ProcessInstance {} completed: {} ", processId, process.getParameters());

                    Long registryId = afterCompletedProcess(process);

                    if (registryId != null) {
                        registryIds.add(registryId);
                    }
                    tmpListProcessInstanesId.remove(processId);
                } else if (process == null) {
                    log.debug("ProcessInstance {} did not find", processId);
                    tmpListProcessInstanesId.remove(processId);
                } else {
                    log.debug("ProcessInstance {} has status {}: {}", new Object[]{processId, process.getState(), process.getParameters()});
                }
            }
            processInstanceIds = tmpListProcessInstanesId;
        } while (!processInstanceIds.isEmpty());

        return registryIds;
    }

	private Long afterCompletedProcess(ProcessInstance process) {
        Map<String, Object> parameters = process.getParameters();
		log.debug("After completed process '{}' parameters: {}", process.getId(), parameters);

        String lastProcessedDate = (String) parameters.get(LAST_PROCESSED_DATE);
        Long serviceProviderId = getLongParameter(parameters, SERVICE_PROVIDER_ID, process.getId());

        ServiceProvider serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(serviceProviderId));

        if (lastProcessedDate != null && serviceProvider != null) {
			ServiceProviderAttribute lastProcessedDateAttribute = getServiceProviderLastProcessedDate(serviceProvider);

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

        return parameters.containsKey(ExportJobParameterNames.REGISTRY_ID)?
				getLongParameter(parameters, ExportJobParameterNames.REGISTRY_ID, process.getId()) : null;
    }

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long createGeneratePaymentRegistryProcess(Organization paymentCollectorOrganization, ServiceProvider serviceProvider) throws ProcessInstanceException, ProcessDefinitionException {
        Organization serviceProviderOrganization = serviceProvider.getOrganization();

        Map<String, Object> parameters = CollectionUtils.map();
		ServiceProviderAttribute lastProcessedDateAttribute = getServiceProviderLastProcessedDate(serviceProvider);

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

		ProcessInstance processInstance = processManager.startProcess(GENERATE_PAYMENTS_REGISTRY_PROCESS, parameters);

		log.debug("process started: {}", processInstance);

		return processInstance != null? processInstance.getId(): 0;
    }

	protected ProcessInstance getWaitingProcess(Long processId) {
		ProcessInstance process;
		process = processManager.getProcessInstance(processId);
		return process;
	}

	protected List<ServiceProvider> getServiceProviders(FetchRange serviceProviderRange) {
		return serviceProviderService.listInstances(serviceProviderRange);
	}

	protected ServiceProviderAttribute getServiceProviderLastProcessedDate(ServiceProvider serviceProvider) {
		return serviceProviderAttributeService
					.getServiceProviderAttribute(stub(serviceProvider), LAST_PROCESSED_DATE);
	}

	private Long getLongParameter(@NotNull Map<String, Object> parameters, String parameterName, Long processInstanceId) {
		Object serviceProviderId = parameters.get(parameterName);
		if (serviceProviderId == null) {
			log.warn("{} do not content in process instance '{}' parameters", parameterName, processInstanceId);
		} else if (serviceProviderId instanceof Long) {
			return (Long)serviceProviderId;
		} else if (serviceProviderId instanceof String) {
			return Long.parseLong((String) serviceProviderId);
		} else {
			log.warn("Can not get {} from process instance variable: {}. Class is {}, but need String or Long.",
					new Object[]{parameterName, serviceProviderId, serviceProviderId.getClass()});
		}
		return null;
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
