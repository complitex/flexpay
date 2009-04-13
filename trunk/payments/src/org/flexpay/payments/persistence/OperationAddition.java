package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.ValueObject;

public class OperationAddition extends ValueObject {

	private OperationAdditionType additionType;
	private Operation operation;

	public OperationAdditionType getAdditionType() {
		return additionType;
	}

	public void setAdditionType(OperationAdditionType additionType) {
		this.additionType = additionType;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}
}
