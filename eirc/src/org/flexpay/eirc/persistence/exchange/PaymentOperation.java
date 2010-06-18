package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryType;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.flexpay.eirc.persistence.exchange.delayed.PaymentOperationDelayedUpdate;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;


public abstract class PaymentOperation extends ContainerOperation {
    protected ServiceOperationsFactory factory;

    protected Long organizationId;

    public PaymentOperation(ServiceOperationsFactory factory, List<String> datum)
		throws InvalidContainerException {

		super(Integer.valueOf(datum.get(0)));
		this.factory = factory;

		try {
			organizationId = Long.parseLong(datum.get(1));
		} catch (Exception e) {
			throw new InvalidContainerException("Invalid payment container", e);
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
		if (!validate(context)) {
			return DelayedUpdateNope.INSTANCE;
		}

		PaymentOperationDelayedUpdate update = getUpdate(context);

        addDocument(context, update);

		return DelayedUpdateNope.INSTANCE;
	}

    private Document addDocument(ProcessingContext context, PaymentOperationDelayedUpdate update) throws FlexPayException {
        RegistryRecord record = context.getCurrentRecord();
        EircRegistryProperties props = (EircRegistryProperties) context.getRegistry().getProperties();

        Document document = new Document();
        setDocumentType(context, document);
        document.setDocumentStatus(factory.getDocumentStatusService().read(DocumentStatus.REGISTERED));
        document.setSumm(record.getAmount());
        document.setService(((EircRegistryRecordProperties)context.getCurrentRecord().getProperties()).getService());
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

        return document;
    }

    private PaymentOperationDelayedUpdate getUpdate(final ProcessingContext context) throws FlexPayException {

		final PaymentOperationDelayedUpdate[] holder = {null};
		// find update first
		context.visitOperationUpdates(new DelayedUpdateVisitor() {
			@Override
			public void apply(DelayedUpdate update) {
                // TODO check operation Id
				if (update instanceof PaymentOperationDelayedUpdate &&
					((PaymentOperationDelayedUpdate)update).getOperationId().equals(getOperationId(context))) {
					holder[0] = (PaymentOperationDelayedUpdate) update;
				}
			}
		});

		PaymentOperationDelayedUpdate update = holder[0];
		if (update == null) {
			update = new PaymentOperationDelayedUpdate(factory.getOperationService(), null);
			update.setOperationId(getOperationId(context));
			org.flexpay.payments.persistence.Operation operation = update.getOperation();
			operation.setOperationSumm(getOperationSum(context));
			setOperationType(context, operation);
			operation.setOperationLevel(factory.getOperationLevelService().read(OperationLevel.AVERAGE));
			operation.setOperationStatus(factory.getOperationStatusService().read(OperationStatus.REGISTERED));

			Organization org = factory.getOrganizationService().readFull(new Stub<Organization>(organizationId));
			if (org == null) {
				throw new FlexPayException("Unknown organization id: " + organizationId);
			}
			operation.setCreatorOrganization(org);

			RegistryRecord record = context.getCurrentRecord();
			operation.setCreationDate(record.getOperationDate());

			context.addUpdate(update);
		}

		return update;
	}

	private void setOperationType(ProcessingContext context, org.flexpay.payments.persistence.Operation operation)
			throws FlexPayException {

		RegistryType type = context.getRegistry().getRegistryType();
		if (type.isCashPayments()) {
			operation.setOperationType(factory.getOperationTypeService().read(OperationType.SERVICE_CASH_PAYMENT));
			return;
		}

		if (type.isCashlessPayments()) {
			operation.setOperationType(factory.getOperationTypeService().read(OperationType.SERVICE_CASHLESS_PAYMENT));
			return;
		}

		throw new FlexPayException("Invalid registry type for payments container: #" + type.getCode());
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

		throw new FlexPayException("Invalid registry type for payments container: #" + type.getCode());
	}

	private boolean validate(ProcessingContext context) throws FlexPayException {
		EircRegistryRecordProperties props = (EircRegistryRecordProperties) context.getCurrentRecord().getProperties();
		if (props.getService() == null) {
			throw new FlexPayException("Cannot create consumer without service set");
		}
		return true;
	}

    abstract Long getOperationId(ProcessingContext context);

    abstract BigDecimal getOperationSum(ProcessingContext context);
}
