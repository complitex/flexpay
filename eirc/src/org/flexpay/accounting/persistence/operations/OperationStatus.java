package org.flexpay.accounting.persistence.operations;

public enum OperationStatus {

	CREATED(1), REGISTERED(2), DELETED(3), RETURNED(4), ERROR(5);

	private final int value;

	OperationStatus(int value) {
		this.value = value;
	}

	// the identifierMethod
	public int toInt() {
		return value;
	}

	// the valueOfMethod
	public static OperationStatus fromInt(int value) {
		switch (value) {
			case 1: return CREATED;
			case 2: return REGISTERED;
			case 3: return DELETED;
			case 4: return RETURNED;
			case 5: return ERROR;
			default:
				return CREATED;
		}
	}

	@Override
	public String toString() {
		switch (this) {
			case CREATED: return "accounting.operation_status.created";
			case REGISTERED: return "accounting.operation_status.registered";
			case DELETED: return "accounting.operation_status.deleted";
			case RETURNED: return "accounting.operation_status.returned";
			case ERROR: return "accounting.operation_status.error";
			default:
					return "accounting.operation_status.created";
		}
	}

}
