package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.SpRegistryType;
import org.flexpay.eirc.service.*;

import java.util.ArrayList;
import java.util.List;

public class ServiceOperationsFactory {

	private SpFileService spFileService;
	private SPService spService;
	private AccountRecordService accountRecordService;
	private OrganisationService organisationService;
	private ReportPeriodService reportPeriodService;

	/**
	 * Get instance of Operation for registry record
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @return Operation instance
	 * @throws InvalidContainerException if record contains invalid operation container
	 *                                   information
	 */
	public Operation getOperation(SpRegistry registry, SpRegistryRecord record) throws FlexPayException {

		if (StringUtils.isEmpty(record.getContainers())) {
			return getOperation(registry);
		}

		List<String> containersData = parseContainersData(record.getContainers());
		List<Operation> operations = new ArrayList<Operation>();
		for (String containerData : containersData) {
			Operation container = fromSingleContainerData(containerData);
			operations.add(container);
		}

		// none containers found, should be defined by registry type
		if (operations.isEmpty()) {
			return getOperation(registry);
		}

		return operations.size() > 1 ? new OperationsChain(operations) : operations.get(0);
	}

	private Operation getOperation(SpRegistry registry) throws FlexPayException {
		int typeId = registry.getRegistryType().getCode();
		if (typeId == SpRegistryType.TYPE_BALANCE) {
			return new BalanceOperation(this);
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

		if (StringUtils.isEmpty(registry.getContainers())) {
			return new NoneOperation();
		}

		List<String> containersData = parseContainersData(registry.getContainers());
		List<Operation> operations = new ArrayList<Operation>();
		for (String containerData : containersData) {
			Operation container = fromSingleContainerData(containerData);
			operations.add(container);
		}

		return operations.size() > 1 ? new OperationsChain(operations) : operations.get(0);
	}

	private Operation fromSingleContainerData(String containerData)
			throws InvalidContainerException {
		List<String> datum = splitEscapableData(containerData, Operation.CONTAINER_DATA_DELIMITER);
		if (datum.size() < 2) {
			throw new InvalidContainerException("Invalid container data: " + containerData);
		}

		Integer containerType = Integer.valueOf(datum.get(0));
		switch (containerType) {
//			case 1:
//				return new OpenAccountOperation(datum);
//			case 2:
//				return new CloseAccountOperation(datum);
//			case 3:
//				return new SetResponsiblePersonOperation(datum);
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

			// Payment
			case 50:
				return new SimplePayment(this, datum);

				// General info
//			case 100:
//				return new SetOverallCheckInfoOperation();
		}

		throw new InvalidContainerException("Unknown container type: " +
											datum.get(0) + " in " + containerData);
	}

	private List<String> parseContainersData(String containers)
			throws InvalidContainerException {

		return splitEscapableData(containers, Operation.CONTAINER_DELIMITER);
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

	public SpFileService getSpFileService() {
		return spFileService;
	}

	public void setSpFileService(SpFileService spFileService) {
		this.spFileService = spFileService;
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

	public void setAccountRecordService(AccountRecordService accountRecordService) {
		this.accountRecordService = accountRecordService;
	}

	public AccountRecordService getAccountRecordService() {
		return accountRecordService;
	}

	public ReportPeriodService getReportPeriodService() {
		return reportPeriodService;
	}

	public void setReportPeriodService(ReportPeriodService reportPeriodService) {
		this.reportPeriodService = reportPeriodService;
	}
}
