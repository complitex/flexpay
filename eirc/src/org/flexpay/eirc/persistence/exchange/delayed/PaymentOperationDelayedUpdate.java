package org.flexpay.eirc.persistence.exchange.delayed;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.exchange.*;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.DocumentAddition;
import org.flexpay.payments.persistence.DocumentAdditionType;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.DocumentAdditionTypeService;
import org.flexpay.payments.service.OperationService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.flexpay.payments.util.config.ApplicationConfig.getMbOrganizationStub;

public class PaymentOperationDelayedUpdate implements
		DelayedUpdate, UpdatesListener, PaymentPointAwareUpdate, ExternalOrganizationAccountAwareUpdate {

	private static final Logger log = LoggerFactory.getLogger(PaymentOperationDelayedUpdate.class);

	private OperationService operationService;
	private DocumentAdditionTypeService additionTypeService;

	private Map<RegistryRecord, Document> record2DocumentMap = CollectionUtils.map();
	private List<ExternalAccountBeforeUpdateSetter> updates = CollectionUtils.list();
	private RegistryRecord currentRecord;
	private Operation operation = new Operation();
	private Long operationId = -1L;

	public PaymentOperationDelayedUpdate(OperationService operationService, DocumentAdditionTypeService additionTypeService) {
		this.operationService = operationService;
		this.additionTypeService = additionTypeService;
		log.debug("New payment operation update created.");
	}

	public Operation getOperation() {
		return operation;
	}

	public void addDocument(Document doc) {
		log.debug("adding document {}", doc);
		operation.addDocument(doc);
		record2DocumentMap.put(currentRecord, doc);
	}

	@NotNull
	public Long getOperationId() {
		return operationId;
	}

	public void setOperationId(@NotNull Long operationId) {
		this.operationId = operationId;
	}

	/**
	 * Do setup payment point for update
	 *
	 * @param point PaymentPoint
	 */
	@Override
	public void setPoint(PaymentPoint point) {
		log.debug("Setting payment point {}", point);
		operation.setPaymentPoint(point);
	}

	/**
	 * Perform storage update
	 *
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if operation fails
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if operation fails
	 */
	@Override
	public void doUpdate() throws FlexPayException, FlexPayExceptionContainer {

		operation.setOperationSum(operation.documentsSum());
		log.debug("Adding payment operation {}", operation);
		operationService.create(operation);
	}

	@Override
	public void setExternalAccount(String accountNumber, Stub<Organization> stubOrg) {

		// delay setup of addition till the update is about to be done
		// in order to remove dependency on containers order
		updates.add(new ExternalAccountBeforeUpdateSetter(currentRecord, accountNumber, stubOrg));
	}

	@Override
	public void nextRecord(ProcessingContext context) {

		currentRecord = context.getCurrentRecord();
	}

	@Override
	public void beforeUpdate(ProcessingContext context) throws FlexPayException {

		for (ExternalAccountBeforeUpdateSetter update : updates) {
			update.doUpdate();
		}
	}

	private final class ExternalAccountBeforeUpdateSetter {

		private RegistryRecord registryRecord;
		private String accountNumber;
		private Stub<Organization> stubOrg;

		private ExternalAccountBeforeUpdateSetter(RegistryRecord registryRecord, String accountNumber, Stub<Organization> stubOrg) {
			this.registryRecord = registryRecord;
			this.accountNumber = accountNumber;
			this.stubOrg = stubOrg;
		}

		void doUpdate() throws FlexPayException {

			Document doc = record2DocumentMap.get(registryRecord);
			if (doc == null) {
				throw new IllegalStateException("Requested external account addition setup, " +
												"but no payment document was associated");
			}

			// TODO create document addition
			if (getMbOrganizationStub().equals(stubOrg)) {
				DocumentAddition addition = new DocumentAddition();
				addition.setAdditionType(additionTypeService.findTypeByCode(DocumentAdditionType.CODE_ERC_ACCOUNT));
				addition.setStringValue(accountNumber);
				doc.addAddition(addition);
			}
		}
	}
}
