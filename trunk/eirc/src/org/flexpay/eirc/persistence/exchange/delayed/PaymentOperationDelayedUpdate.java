package org.flexpay.eirc.persistence.exchange.delayed;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.exchange.*;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.OperationService;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Map;
import java.util.List;

public class PaymentOperationDelayedUpdate implements
		DelayedUpdate, UpdatesListener, PaymentPointAwareUpdate, ExternalOrganizationAccountAwareUpdate {

	private OperationService operationService;

	private Map<RegistryRecord, Document> record2DocumentMap = CollectionUtils.map();
	private List<ExternalAccountBeforeUpdateSetter> updates = CollectionUtils.list();
	private RegistryRecord currentRecord;
	private Operation operation = new Operation();
	private Long operationId = -1L;

	public PaymentOperationDelayedUpdate(OperationService operationService) {
		this.operationService = operationService;
	}

	public Operation getOperation() {
		return operation;
	}

	public void addDocument(Document doc) {
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

		BigDecimal summ = operation.documentsSumm();
		if (!operation.getOperationSumm().equals(summ)) {
			throw new FlexPayException("Summ mismatch, expected " + operation.getOperationSumm() +
									   ", but found " + summ);
		}
		operationService.create(operation);
	}

	@Override
	public void setExternalAccount(String accountNumber, Organization org) {

		// delay setup of addition till the update is about to be done
		// in order to remove dependency on containers order
		updates.add(new ExternalAccountBeforeUpdateSetter(currentRecord, accountNumber, org));
	}

	@Override
	public void nextRecord(ProcessingContext context) {

		currentRecord = context.getCurrentRecord();
	}

	@Override
	public void beforeUpdate(ProcessingContext context) {

		for (ExternalAccountBeforeUpdateSetter update : updates) {
			update.doUpdate();
		}
	}

	private final class ExternalAccountBeforeUpdateSetter {

		private RegistryRecord registryRecord;
		private String accountNumber;
		private Organization org;

		private ExternalAccountBeforeUpdateSetter(RegistryRecord registryRecord, String accountNumber, Organization org) {
			this.registryRecord = registryRecord;
			this.accountNumber = accountNumber;
			this.org = org;
		}

		void doUpdate() {

			Document doc = record2DocumentMap.get(registryRecord);
			if (doc == null) {
				throw new IllegalStateException("Requested external account addition setup, " +
												"but no payment document was associated");
			}

			// TODO create document addition
		}

	}
}
