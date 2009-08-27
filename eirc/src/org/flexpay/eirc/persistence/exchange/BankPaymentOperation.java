package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryType;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.exchange.delayed.PaymentOperationDelayedUpdate;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.orgs.persistence.Organization;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class BankPaymentOperation extends ContainerOperation {

	private ServiceOperationsFactory factory;
	private Long organizationId;
	private Long operationId;
	private BigDecimal summ;

	public BankPaymentOperation(ServiceOperationsFactory factory, List<String> datum)
		throws InvalidContainerException {

		super(Integer.valueOf(datum.get(0)));
		this.factory = factory;

		try {
			organizationId = Long.parseLong(datum.get(1));
			operationId = Long.parseLong(datum.get(2));
			summ = new BigDecimal(datum.get(3));
		} catch (Exception e) {
			throw new InvalidContainerException("Invalid bank payment container", e);
		}
	}

	/**
	 * Process operation
	 *
	 * @param context ProcessingContext
	 * @return DelayedUpdate object
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if failure occurs
	 */
	@Override
	public DelayedUpdate process(@NotNull ProcessingContext context)
			throws FlexPayException, FlexPayExceptionContainer {

		PaymentOperationDelayedUpdate update = getUpdate(context);

		RegistryRecord record = context.getCurrentRecord();
		EircRegistryProperties props = (EircRegistryProperties) context.getRegistry().getProperties();

		Document document = new Document();
		setDocumentType(context, document);
		document.setDocumentStatus(factory.getDocumentStatusService().read(DocumentStatus.REGISTERED));
		document.setSumm(record.getAmount());
		document.setService(factory.getConsumerService().findService(
				props.getServiceProviderStub(), record.getServiceCode()));
		document.setCreditorOrganization(props.getSender());
		document.setDebtorOrganization(props.getRecipient());
		document.setDebtorId(record.getPersonalAccountExt());

		document.setLastName(record.getLastName());
		document.setMiddleName(record.getMiddleName());
		document.setFirstName(record.getFirstName());
		document.setTown(record.getCity());
		document.setStreetType(record.getStreetType());
		document.setStreetName(record.getStreetName());
		document.setBuildingNumber(record.getBuildingNum());
		document.setBuildingBulk(record.getBuildingBulkNum());
		document.setApartmentNumber(record.getApartmentNum());

		update.addDocument(document);

		return DelayedUpdateNope.INSTANCE;
	}

	private PaymentOperationDelayedUpdate getUpdate(ProcessingContext context) throws FlexPayException {

		final PaymentOperationDelayedUpdate[] holder = {null};
		// find update first
		context.visitOperationUpdates(new DelayedUpdateVisitor() {
			@Override
			public void apply(DelayedUpdate update) {
				if (update instanceof PaymentOperationDelayedUpdate) {
					holder[0] = (PaymentOperationDelayedUpdate) update;
				}
			}
		});

		PaymentOperationDelayedUpdate update = holder[0];
		if (update == null) {
			update = new PaymentOperationDelayedUpdate(factory.getOperationService());
			update.setOperationId(operationId);
			org.flexpay.payments.persistence.Operation operation = update.getOperation();
			operation.setOperationSumm(summ);
			setOperationType(context, operation);
			operation.setOperationLevel(factory.getOperationLevelService().read(OperationLevel.AVERAGE));
			operation.setOperationStatus(factory.getOperationStatusService().read(OperationStatus.REGISTERED));

			Organization org = factory.getOrganizationService().readFull(new Stub<Organization>(organizationId));
			if (org == null) {
				throw new FlexPayException("Unknown organization id: " + organizationId);
			}
			update.getOperation().setCreatorOrganization(org);

			RegistryRecord record = context.getCurrentRecord();
			update.getOperation().setCreationDate(record.getOperationDate());

			context.addUpdate(update);
		}

		return update;
	}

	private void setOperationType(ProcessingContext context, Operation operation) throws FlexPayException {

		RegistryType type = context.getRegistry().getRegistryType();
		if (type.isCashPayments()) {
			operation.setOperationType(factory.getOperationTypeService().read(OperationType.SERVICE_CASH_PAYMENT));
			return;
		}

		if (type.isCashlessPayments()) {
			operation.setOperationType(factory.getOperationTypeService().read(OperationType.SERVICE_CASHLESS_PAYMENT));
			return;
		}

		throw new FlexPayException("Invalid registry type for bank payments container: #" + type.getCode());
	}

	private void setDocumentType(ProcessingContext context, Document document) throws FlexPayException {

		RegistryType type = context.getRegistry().getRegistryType();
		if (type.isCashPayments()) {
			document.setDocumentType(factory.getDocumentTypeService().read(DocumentType.CASH_PAYMENT));
			return;
		}

		if (type.isCashlessPayments()) {
			document.setDocumentType(factory.getDocumentTypeService().read(DocumentType.CASHLESS_PAYMENT));
			return;
		}

		throw new FlexPayException("Invalid registry type for bank payments container: #" + type.getCode());
	}
}
