package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.flexpay.bti.service.ApartmentAttributeTypeService;
import org.flexpay.bti.service.BtiApartmentService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.service.RegistryFileService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.ImportErrorService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.persistence.exchange.conditions.ConditionsFactory;
import org.flexpay.eirc.service.*;
import org.flexpay.eirc.service.importexport.ImportUtil;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class ServiceOperationsFactory {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private OperationService operationService;
	private OperationLevelService operationLevelService;
	private OperationTypeService operationTypeService;
	private OperationStatusService operationStatusService;
	private DocumentStatusService documentStatusService;
	private DocumentTypeService documentTypeService;
	private PaymentPointService paymentPointService;
	private RegistryFileService registryFileService;
	private SPService spService;
	private EircAccountService accountService;
	private ConsumerService consumerService;
	private QuittanceService quittanceService;
	private ConsumerInfoService consumerInfoService;
	private ConsumerAttributeTypeService consumerAttributeTypeService;
	private OrganizationService organizationService;
	private ServiceProviderService serviceProviderService;
	private ReportPeriodService reportPeriodService;
	private CorrectionsService correctionsService;
	private MasterIndexService masterIndexService;
	private ClassToTypeRegistry classToTypeRegistry;
	private BtiApartmentService btiApartmentService;
	private ApartmentAttributeTypeService apartmentAttributeTypeService;

	private ConditionsFactory conditionsFactory;

	private ImportErrorService importErrorService;
	private ImportUtil importUtil;

	/**
	 * Get instance of Operation for registry record
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @return Operation instance
	 * @throws InvalidContainerException if record contains invalid operation container information
	 */
	public Operation getOperation(Registry registry, RegistryRecord record) throws FlexPayException {

		List<RegistryRecordContainer> containers = record.getContainers();
		if (containers.isEmpty()) {
			return getOperation(registry);
		}

		// get a list of operations
		List<Operation> operations = list();
		for (RegistryRecordContainer container : containers) {
			if (container == null || StringUtils.isEmpty(container.getData())) {
				continue;
			}
			Operation operation = fromSingleContainerData(registry, container.getData());
			operations.add(operation);
		}

		// none containers found, should be defined by registry type
		if (operations.isEmpty()) {
			return getOperation(registry);
		}

		return operations.size() > 1 ? new OperationsChain(operations) : operations.get(0);
		/*
		return new Operation() {

			@Override
			public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException, FlexPayExceptionContainer {
				log.debug("sleep start");
				try {
					for (int i = 0; i < 7; i++) {
						Thread.sleep(RandomUtils.nextInt(200));
					}
				} catch (InterruptedException e) {
					log.warn("Interrupted", e);
				}
				log.debug("sleep end");
				return DelayedUpdateNope.INSTANCE;
			}
		};
		*/
	}

	private Operation getOperation(Registry registry) throws FlexPayException {
		int typeId = registry.getRegistryType().getCode();
		switch (typeId) {
		}

		throw new UnsupportedRegistryTypeException("Registry type: " + typeId + " is not supported");
	}

	/**
	 * Get instance of OperationContainer for registry record
	 *
	 * @param registry SpRegistry
	 * @return OperationContainer instance
	 * @throws InvalidContainerException if record contains invalid operation container information
	 */
	public Operation getContainerOperation(Registry registry) throws InvalidContainerException {

		if (registry.getContainers().isEmpty()) {
			log.debug("No registry containers found");
			return new NoneOperation();
		}

		List<RegistryContainer> containers = registry.getContainers();
		List<Operation> operations = list();
		for (RegistryContainer container : containers) {
			if (container == null || StringUtils.isEmpty(container.getData())) {
				continue;
			}
			Operation operation = fromSingleContainerData(registry, container.getData());
			operations.add(operation);
		}

		return operations.size() > 1 ? new OperationsChain(operations) : operations.get(0);
	}

	private Operation fromSingleContainerData(Registry registry, String containerData)
			throws InvalidContainerException {

		List<String> datum = splitEscapableData(containerData, Operation.CONTAINER_DATA_DELIMITER);
		if (datum.size() < 2) {
			throw new InvalidContainerException("Invalid container data: " + containerData);
		}

		Integer containerType = Integer.valueOf(datum.get(0));
		switch (containerType) {
			case 1:
				checkContainer(registry, "Open account", RegistryType.TYPE_INFO);
				return new OpenAccountOperation(this, datum);
			case 2:
				checkContainer(registry, "Close account", RegistryType.TYPE_CLOSED_ACCOUNTS);
				return new CloseAccountOperation(this, datum);
			case 3:
				checkContainer(registry, "Set responsible person", RegistryType.TYPE_INFO);
				return new SetResponsiblePersonOperation(this, datum);
			case 4:
				checkContainer(registry, "Set number on habitants", RegistryType.TYPE_INFO);
				return new SetNumberOfHabitantsOperation(this, datum);
			case 5:
				checkContainer(registry, "Set total square", RegistryType.TYPE_INFO);
				return new SetTotalSquareOperation(this, datum);
			case 6:
				checkContainer(registry, "Set live square", RegistryType.TYPE_INFO);
				return new SetLiveSquareOperation(this, datum);
			case 7:
				checkContainer(registry, "Set warm square", RegistryType.TYPE_INFO);
				return new SetWarmSquareOperation(datum);
			case 8:
				checkContainer(registry, "Set privilege type", RegistryType.TYPE_INFO);
				return new SetPrivilegeTypeOperation(datum);
			case 9:
				checkContainer(registry, "Set privilege owner", RegistryType.TYPE_INFO);
				return new SetPrivilegeOwnerOperation(datum);
			case 10:
				checkContainer(registry, "Set privilege person", RegistryType.TYPE_INFO);
				return new SetPrivilegePersonOperation(datum);
			case 11:
				checkContainer(registry, "Set privilege approval document", RegistryType.TYPE_INFO);
				return new SetPrivilegeApprovalDocumentOperation(datum);
			case 12:
				checkContainer(registry, "Set privilege persons number", RegistryType.TYPE_INFO);
				return new SetPrivilegePersonsNumberOperation(datum);
			case 14:
				checkContainer(registry, "Open subaccount", RegistryType.TYPE_INFO);
				return new OpenSubserviceAccountOperation(this, datum);
			case 15:
				return new SetExternalOrganizationAccountOperation(this, datum);

			// Payment
			case 50:
				checkContainer(registry, "Simple payment", RegistryType.TYPE_CASH_PAYMENTS);
				return new SimplePaymentOperation(this, datum);
			case 52:
				throw new InvalidContainerException("Bank payments container is outgoing, should not be processed");

			// General info
			case 100:
				return new BaseContainerOperation(this, datum);

			case 500:
				return new PaymentPointSetupOperation(this, datum);
			case 502:
				return new ObjectIdentifierSyncOperation(this, datum);
            case 503:
                return new InstanceIdOperation(datum);

			// Calculation properties
			case 600:
				checkContainer(registry, "Set number tenants of the house (calculation property)", RegistryType.TYPE_INFO);
				return new SetNumberTenantsOperation(this, datum);
			case 601:
				checkContainer(registry, "Set number registered tenants of the house (calculation property)", RegistryType.TYPE_INFO);
				return new SetNumberRegisteredTenantsOperation(this, datum);
			case 602:
				checkContainer(registry, "Set total square of the house (calculation property)", RegistryType.TYPE_INFO);
				return new SetTotalSquareCalculationPropertyOperation(this, datum);
			case 603:
				checkContainer(registry, "Set live square of the house (calculation property)", RegistryType.TYPE_INFO);
				return new SetLiveSquareCalculationPropertyOperation(this, datum);
			case 604:
				checkContainer(registry, "Set heating square of the house (calculation property)", RegistryType.TYPE_INFO);
				return new SetHeatingSquareCalculationPropertyOperation(this, datum);
		}

		throw new InvalidContainerException("Unknown container type: " +
											datum.get(0) + " in " + containerData);
	}

	private void checkContainer(Registry registry, String name, int type) throws InvalidContainerException {
		if (registry.getRegistryType().getCode() != type) {
			throw new InvalidContainerException(name + " containers are not allowed in registry type " + type);
		}
	}

	/**
	 * Split string with delimiter taking in account {@link Operation#ESCAPE_SYMBOL}
	 *
	 * @param containers Containers data
	 * @param delimiter  Delimiter symbol
	 * @return List of separate containers
	 */
	private List<String> splitEscapableData(String containers, char delimiter) {
		return StringUtil.splitEscapable(containers, delimiter, Operation.ESCAPE_SYMBOL);
	}

	public RegistryFileService getSpFileService() {
		return registryFileService;
	}

	@Required
	public void setSpFileService(RegistryFileService registryFileService) {
		this.registryFileService = registryFileService;
	}

	public SPService getSpService() {
		return spService;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	public OrganizationService getOrganizationService() {
		return organizationService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	public ReportPeriodService getReportPeriodService() {
		return reportPeriodService;
	}

	@Required
	public void setReportPeriodService(ReportPeriodService reportPeriodService) {
		this.reportPeriodService = reportPeriodService;
	}

	public EircAccountService getAccountService() {
		return accountService;
	}

	@Required
	public void setAccountService(EircAccountService accountService) {
		this.accountService = accountService;
	}

	public ConsumerService getConsumerService() {
		return consumerService;
	}

	@Required
	public void setConsumerService(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}

	public CorrectionsService getCorrectionsService() {
		return correctionsService;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	public ImportErrorService getImportErrorService() {
		return importErrorService;
	}

	@Required
	public void setImportErrorService(ImportErrorService importErrorService) {
		this.importErrorService = importErrorService;
	}

	public ConsumerInfoService getConsumerInfoService() {
		return consumerInfoService;
	}

	@Required
	public void setConsumerInfoService(ConsumerInfoService consumerInfoService) {
		this.consumerInfoService = consumerInfoService;
	}

	public QuittanceService getQuittanceService() {
		return quittanceService;
	}

	@Required
	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}

	public ImportUtil getImportUtil() {
		return importUtil;
	}

	@Required
	public void setImportUtil(ImportUtil importUtil) {
		this.importUtil = importUtil;
	}

	public ConditionsFactory getConditionsFactory() {
		return conditionsFactory;
	}

	@Required
	public void setConditionsFactory(ConditionsFactory conditionsFactory) {
		this.conditionsFactory = conditionsFactory;
	}

	public ServiceProviderService getServiceProviderService() {
		return serviceProviderService;
	}

	@Required
	public void setServiceProviderService(ServiceProviderService serviceProviderService) {
		this.serviceProviderService = serviceProviderService;
	}

	public BtiApartmentService getBtiApartmentService() {
		return btiApartmentService;
	}

	@Required
	public void setBtiApartmentService(BtiApartmentService btiApartmentService) {
		this.btiApartmentService = btiApartmentService;
	}

	public ApartmentAttributeTypeService getApartmentAttributeTypeService() {
		return apartmentAttributeTypeService;
	}

	@Required
	public void setApartmentAttributeTypeService(ApartmentAttributeTypeService apartmentAttributeTypeService) {
		this.apartmentAttributeTypeService = apartmentAttributeTypeService;
	}

	public ConsumerAttributeTypeService getConsumerAttributeTypeService() {
		return consumerAttributeTypeService;
	}

	@Required
	public void setConsumerAttributeTypeService(ConsumerAttributeTypeService consumerAttributeTypeService) {
		this.consumerAttributeTypeService = consumerAttributeTypeService;
	}

	public OperationService getOperationService() {
		return operationService;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}

	public PaymentPointService getPaymentPointService() {
		return paymentPointService;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	public OperationLevelService getOperationLevelService() {
		return operationLevelService;
	}

	@Required
	public void setOperationLevelService(OperationLevelService operationLevelService) {
		this.operationLevelService = operationLevelService;
	}

	public OperationTypeService getOperationTypeService() {
		return operationTypeService;
	}

	@Required
	public void setOperationTypeService(OperationTypeService operationTypeService) {
		this.operationTypeService = operationTypeService;
	}

	public OperationStatusService getOperationStatusService() {
		return operationStatusService;
	}

	@Required
	public void setOperationStatusService(OperationStatusService operationStatusService) {
		this.operationStatusService = operationStatusService;
	}

	public DocumentStatusService getDocumentStatusService() {
		return documentStatusService;
	}

	@Required
	public void setDocumentStatusService(DocumentStatusService documentStatusService) {
		this.documentStatusService = documentStatusService;
	}

	public DocumentTypeService getDocumentTypeService() {
		return documentTypeService;
	}

	@Required
	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	public MasterIndexService getMasterIndexService() {
		return masterIndexService;
	}

	@Required
	public void setMasterIndexService(MasterIndexService masterIndexService) {
		this.masterIndexService = masterIndexService;
	}

	public ClassToTypeRegistry getClassToTypeRegistry() {
		return classToTypeRegistry;
	}

	@Required
	public void setClassToTypeRegistry(ClassToTypeRegistry classToTypeRegistry) {
		this.classToTypeRegistry = classToTypeRegistry;
	}
}
