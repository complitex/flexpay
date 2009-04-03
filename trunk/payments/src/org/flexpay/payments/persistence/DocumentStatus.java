package org.flexpay.payments.persistence;

public enum DocumentStatus {

	CREATED(1), REGISTERED(2), DELETED(3), RETURNED(4), ERROR(5);

	private final int value;

	DocumentStatus(int value) {
		this.value = value;
	}

	// the identifierMethod
	public int toInt() {
		return value;
	}

	// the valueOfMethod
	public static DocumentStatus fromInt(int value) {
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
			case CREATED: return "accounting.document_status.created";
			case REGISTERED: return "accounting.document_status.registered";
			case DELETED: return "accounting.document_status.deleted";
			case RETURNED: return "accounting.document_status.returned";
			case ERROR: return "accounting.document_status.error";
			default:
					return "accounting.document_status.created";
		}
	}

}
