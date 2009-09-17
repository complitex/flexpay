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
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.ServiceProviderAttribute;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.process.export.job.ExportJobParameterNames;
import org.flexpay.orgs.service.ServiceProviderAttributeService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.Serializable;
import java.util.*;

/**
 * Scheduling job generate payments registries for all service providers and registered oraganizations.
 */
public class GeneratePaymentsRegistry extends QuartzJobBean {

	private Logger log = LoggerFactory.getLogger(getClass());

	private static final String USER_PAYMENTS_REGISTRY_GENERATOR = "payments-registry-generator";
    private static final String GENERATE_PAYMENTS_REGISRY_PROCESS = "GeneratePaymentsRegisryProcess";
    
	// time out 10 sec
	private static final long TIME_OUT = 10000;
	private static final Integer PAGE_SIZE = 20;

	private String privateKey;

	private ProcessManager processManager;
	private ServiceProviderService serviceProviderService;
	private ServiceProviderAttributeService serviceProviderAttributeService;
	private PaymentCollectorService paymentCollectorService;

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
			org.flexpay.orgs.service.Roles.PAYMENT_COLLECTOR_READ
	);

	/**
     * Start processes "GeneratePaymentsRegisryProcess" for all existed in database service providers and registred organization.<br/>
     * Job wait while all started processes will finish and add generated registries ids to job execution context.<br/>
     * Registries ids content in {@link org.quartz.JobExecutionContext#getMergedJobDataMap()}.
     * Mapping key is "registries" and value`s type is {@link java.util.List}<{@link Long}>
     *
     * @param context Job execution context.
     * @throws JobExecutionException
     */
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		log.debug("Starting process generate payments registry at {}", new Date());

		try {
			authenticatePaymentsRegistryGenerator();

			Page<PaymentCollector> paymentCollectorPage = new Page<PaymentCollector>(PAGE_SIZE);
			List<PaymentCollector> listPaymentCollectors;

			Page<ServiceProvider> serviceProvidersPage = new Page<ServiceProvider>(PAGE_SIZE);
			List<ServiceProvider> listServiceProviders;
			List<Long> listProcessInstanesId = new ArrayList<Long>();
			List<Map<Serializable, Serializable>> waitingProcessData = new ArrayList<Map<Serializable, Serializable>>();

			while ((listPaymentCollectors = paymentCollectorService.listInstances(paymentCollectorPage)).size() > 0) {
				for (PaymentCollector paymentCollector : listPaymentCollectors) {
					log.debug("Payment collector (registered) organization {}", paymentCollector.getOrganization().getId());

					serviceProvidersPage.setPageNumber(1);
					while ((listServiceProviders = serviceProviderService.listInstances(serviceProvidersPage)).size() > 0) {
                        log.debug("number service providers page {}, number service providers {}", new Object[]{serviceProvidersPage.getPageNumber(), listServiceProviders.size()});
						for (ServiceProvider serviceProvider : listServiceProviders) {
							Organization organization = serviceProvider.getOrganization();
							if (organization != null && organization.getId() != null) {
								Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
								ServiceProviderAttribute lastProcessedDateAttribute = serviceProviderAttributeService.getServiceProviderAttribute(Stub.stub(serviceProvider), ExportJobParameterNames.LAST_PROCESSED_DATE);

								if (lastProcessedDateAttribute != null) {
									parameters.put(lastProcessedDateAttribute.getName(), lastProcessedDateAttribute.getValue());
								}

								Date finishDate = DateUtil.now();
								parameters.put(ExportJobParameterNames.FINISH_DATE, finishDate);
								parameters.put(ExportJobParameterNames.ORGANIZATION_ID, organization.getId());
								parameters.put(ExportJobParameterNames.SERVICE_PROVIDER_ID, serviceProvider.getId());
								parameters.put(ExportJobParameterNames.REGISTERED_ORGANIZATION_ID, paymentCollector.getOrganization().getId());
								//parameters.put(ExportJobParameterNames.EMAIL, serviceProvider.getEmail());
								parameters.put(ExportJobParameterNames.PRIVATE_KEY, privateKey);

								waitingProcessData.add(parameters);

                                log.debug("start process {}", parameters);
								long processId = processManager.createProcess(GENERATE_PAYMENTS_REGISRY_PROCESS, parameters);
								listProcessInstanesId.add(processId);
							} else {
								log.error("Organization did not find for service provider with id {}", serviceProvider.getId());
							}
						}
						serviceProvidersPage.nextPage();
					}
				}
				paymentCollectorPage.nextPage();
			}

			List<Long> registries = new ArrayList<Long>();

			do {
                log.debug("Waiting number {} processes: {}", new Object[]{GENERATE_PAYMENTS_REGISRY_PROCESS, waitingProcessData.size()});
				for (Map<Serializable, Serializable> param : waitingProcessData) {
					log.debug("Waiting {} processes will complete for: {} ...", new Object[]{GENERATE_PAYMENTS_REGISRY_PROCESS, param});
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

						String lastProcessedDate = (String) parameters.get(ExportJobParameterNames.LAST_PROCESSED_DATE);
						Long serviceProviderId = (Long) parameters.get(ExportJobParameterNames.SERVICE_PROVIDER_ID);
						ServiceProvider serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(serviceProviderId));

						if (lastProcessedDate != null && serviceProvider != null) {
							ServiceProviderAttribute lastProcessedDateAttribute = serviceProviderAttributeService.getServiceProviderAttribute(Stub.stub(serviceProvider), ExportJobParameterNames.LAST_PROCESSED_DATE);

							if (lastProcessedDateAttribute == null) {
								lastProcessedDateAttribute = new ServiceProviderAttribute();
								lastProcessedDateAttribute.setServiceProvider(serviceProvider);
								lastProcessedDateAttribute.setName(ExportJobParameterNames.LAST_PROCESSED_DATE);
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

						if (parameters.containsKey(ExportJobParameterNames.REGISTRY_ID)) {
							Long registryId = (Long) parameters.get(ExportJobParameterNames.REGISTRY_ID);
							if (registryId != null) {
								registries.add(registryId);
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
			context.getMergedJobDataMap().put(ExportJobParameterNames.REGISTRIES, registries);
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
	public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
		this.paymentCollectorService = paymentCollectorService;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
}
