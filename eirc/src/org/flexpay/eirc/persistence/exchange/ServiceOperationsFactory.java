package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ServiceOperationsFactory {

	/**
	 * Get instance of OperationContainer for registry record
	 *
	 * @param record Registry record
	 * @return OperationContainer instance
	 * @throws InvalidContainerException if record contains invalid operation container
	 *                                   information
	 */
	public Operation getContainer(SpRegistryRecord record) throws InvalidContainerException {

		if (StringUtils.isEmpty(record.getContainers())) {
			throw new InvalidContainerException("No data in record #" + record.getId());
		}

		List<String> containersData = parseContainersData(record.getContainers());
		List<Operation> operations = new ArrayList<Operation>();
		for (String containerData : containersData) {
			Operation container = fromSingleContainerData(containerData);
			operations.add(container);
		}

		return operations.size() > 1 ? new OperationsChain(operations) : operations.get(0);
	}

	/**
	 * Get instance of OperationContainer for registry record
	 *
	 * @param registry SpRegistry 
	 * @return OperationContainer instance
	 * @throws InvalidContainerException if record contains invalid operation container
	 *                                   information
	 */
	public Operation getContainer(SpRegistry registry) throws InvalidContainerException {

		if (StringUtils.isEmpty(registry.getContainers())) {
			throw new InvalidContainerException("No data in registry #" + registry.getId());
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
				return new SimplePayment(datum);

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
	 * @param delimiter Delimiter simbol
	 * @return List of separate containers
	 */
	private List<String> splitEscapableData(String containers, char delimiter) {

		return StringUtil.splitEscapable(containers, delimiter, Operation.ESCAPE_SIMBOL);
	}
}
