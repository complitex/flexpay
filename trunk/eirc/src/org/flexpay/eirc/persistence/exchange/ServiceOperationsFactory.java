package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.ImportErrorService;
import org.flexpay.common.service.importexport.ImportErrorsSupport;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.dao.RegistryRecordDao;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.persistence.exchange.conditions.ConditionsFactory;
import org.flexpay.eirc.service.*;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.flexpay.eirc.service.importexport.ImportUtil;
import org.flexpay.eirc.service.importexport.imp.ClassToTypeRegistry;

import java.util.ArrayList;
import java.util.List;

public class ServiceOperationsFactory {

	private RegistryFileService registryFileService;
	private SPService spService;
	private EircAccountService accountService;
	private ConsumerService consumerService;
	private QuittanceService quittanceService;
	private ConsumerInfoService consumerInfoService;
	private OrganisationService organisationService;
	private ReportPeriodService reportPeriodService;
	private CorrectionsService correctionsService;

	private ConditionsFactory conditionsFactory;

	private ClassToTypeRegistry registry;
	private ImportErrorsSupport errorsSupport;
	private ImportErrorService importErrorService;
	private RawDataSource<RawConsumerData> dataSource;
	private RegistryRecordDao registryRecordDao;
	private ImportUtil importUtil;

	/**
	 * Get instance of Operation for registry record
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @return Operation instance
	 * @throws InvalidContainerException if record contains invalid operation container
	 *                                   information
	 */
	public Operation getOperation(SpRegistry registry, RegistryRecord record) throws FlexPayException {

		List<RegistryRecordContainer> containers = record.getContainers();
		if (containers.isEmpty()) {
			return getOperation(registry);
		}

		// get a list of operations
		List<Operation> operations = new ArrayList<Operation>();
		for (RegistryRecordContainer container : containers) {
			Operation operation = fromSingleContainerData(registry, container.getData());
			operations.add(operation);
		}

		// none containers found, should be defined by registry type
		if (operations.isEmpty()) {
			return getOperation(registry);
		}

		return operations.size() > 1 ? new OperationsChain(operations) : operations.get(0);
	}

	private Operation getOperation(SpRegistry registry) throws FlexPayException {
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
	 * @throws InvalidContainerException if record contains invalid operation container
	 *                                   information
	 */
	public Operation getContainerOperation(SpRegistry registry) throws InvalidContainerException {

		if (registry.getContainers().isEmpty()) {
			return new NoneOperation();
		}

		List<RegistryContainer> containers = registry.getContainers();
		List<Operation> operations = new ArrayList<Operation>();
		for (RegistryContainer containerData : containers) {
			Operation container = fromSingleContainerData(registry, containerData.getData());
			operations.add(container);
		}

		return operations.size() > 1 ? new OperationsChain(operations) : operations.get(0);
	}

	private Operation fromSingleContainerData(SpRegistry registry, String containerData)
			throws InvalidContainerException {
		List<String> datum = splitEscapableData(containerData, Operation.CONTAINER_DATA_DELIMITER);
		if (datum.size() < 2) {
			throw new InvalidContainerException("Invalid container data: " + containerData);
		}

		Integer containerType = Integer.valueOf(datum.get(0));
		switch (containerType) {
			case 1:
				return new OpenAccountOperation(this, datum);
			case 2:
				if (registry.getRegistryType().getCode() != RegistryType.TYPE_CLOSED_ACCOUNTS) {
					throw new InvalidContainerException("Close account containers are allowed only in close accounts registry");
				}
				return new CloseAccountOperation(this, datum);
			case 3:
				return new SetResponsiblePersonOperation(this, datum);
//			case 4:
//				return new SetNumberOfHabitantsOperation(datum);
//			case 5:
//				return new SetTotalSquareOperation(datum);
//			case 6:
//				return new SetLiveSquareOperation(datum);
//			case 7:
//				return new SetWarmSquareOperation(datum);
//			case 8:
//				return new SetPrivilegeTypeOperation(datum);
//			case 9:
//				return new SetPrivilegeOwnerOperation(datum);
//			case 10:
//				return new SetPrivilegePersonOperation(datum);
//			case 11:
//				return new SetPrivilegeApprovalDocumentOperation(datum);
//			case 12:
//				return new SetPrivilegePersonsNumberOperation(datum);
			case 14:
				return new OpenSubserviceAccountOperation(this, datum);

				// Payment
//			case 50:

				// General info
			case 100:
				return new BaseContainerOperation(this, datum);
		}

		throw new InvalidContainerException("Unknown container type: " +
				datum.get(0) + " in " + containerData);
	}

	/**
	 * Split string with delimiter taking in account {@link Operation#ESCAPE_SIMBOL}
	 *
	 * @param containers Containers data
	 * @param delimiter  Delimiter simbol
	 * @return List of separate containers
	 */
	private List<String> splitEscapableData(String containers, char delimiter) {

		return StringUtil.splitEscapable(containers, delimiter, Operation.ESCAPE_SIMBOL);
	}

	public ImportError addImportError(SpRegistry spRegistry, RegistryRecord record,
									  Class<? extends DomainObject> clazz, String errorCode) {

		ImportError error = new ImportError();
		error.setSourceDescription(spRegistry.getServiceProvider().getDataSourceDescription());
		error.setSourceObjectId(String.valueOf(record.getId()));
		error.setErrorId(errorCode);
		error.setObjectType(registry.getType(clazz));
		errorsSupport.setDataSourceBean(error, dataSource);

		importErrorService.addError(error);

		record.setImportError(error);
		registryRecordDao.update(record);

		return error;
	}

	public void setDataSource(RawDataSource<RawConsumerData> dataSource) {
		this.dataSource = dataSource;
	}

	public RawDataSource<RawConsumerData> getDataSource() {
		return dataSource;
	}

	public RegistryFileService getSpFileService() {
		return registryFileService;
	}

	public void setSpFileService(RegistryFileService registryFileService) {
		this.registryFileService = registryFileService;
	}

	public SPService getSpService() {
		return spService;
	}

	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}

	public OrganisationService getOrganisationService() {
		return organisationService;
	}

	public ReportPeriodService getReportPeriodService() {
		return reportPeriodService;
	}

	public void setReportPeriodService(ReportPeriodService reportPeriodService) {
		this.reportPeriodService = reportPeriodService;
	}

	public EircAccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(EircAccountService accountService) {
		this.accountService = accountService;
	}

	public ConsumerService getConsumerService() {
		return consumerService;
	}

	public void setConsumerService(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}

	public CorrectionsService getCorrectionsService() {
		return correctionsService;
	}

	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	public ImportErrorService getImportErrorService() {
		return importErrorService;
	}

	public void setImportErrorService(ImportErrorService importErrorService) {
		this.importErrorService = importErrorService;
	}

	public void setRegistry(ClassToTypeRegistry registry) {
		this.registry = registry;
	}

	public void setErrorsSupport(ImportErrorsSupport errorsSupport) {
		this.errorsSupport = errorsSupport;
	}

	public void setRegistryRecordDao(RegistryRecordDao registryRecordDao) {
		this.registryRecordDao = registryRecordDao;
	}

	public ConsumerInfoService getConsumerInfoService() {
		return consumerInfoService;
	}

	public void setConsumerInfoService(ConsumerInfoService consumerInfoService) {
		this.consumerInfoService = consumerInfoService;
	}

	public QuittanceService getQuittanceService() {
		return quittanceService;
	}

	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}

	public ImportUtil getImportUtil() {
		return importUtil;
	}

	public void setImportUtil(ImportUtil importUtil) {
		this.importUtil = importUtil;
	}

	public ConditionsFactory getConditionsFactory() {
		return conditionsFactory;
	}

	public void setConditionsFactory(ConditionsFactory conditionsFactory) {
		this.conditionsFactory = conditionsFactory;
	}
}
