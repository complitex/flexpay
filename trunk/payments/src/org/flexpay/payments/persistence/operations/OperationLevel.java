package org.flexpay.payments.persistence.operations;

public enum OperationLevel {

	LOWEST(1), LOW(2), AVERAGE(3), HIGH(4), SUSPENDED(5);

	private final int value;

	OperationLevel(int value) {
		this.value = value;
	}

	// the identifierMethod
	public int toInt() {
		return value;
	}

	// the valueOfMethod
	public static OperationLevel fromInt(int value) {
		switch (value) {
			case 1: return LOWEST;
			case 2: return LOW;
			case 3: return AVERAGE;
			case 4: return HIGH;
			case 5: return SUSPENDED;
			default:
				return LOWEST;
		}
	}

	@Override
	public String toString() {
		switch (this) {
			case LOWEST: return "accounting.operation_level.lowest";
			case LOW: return "accounting.operation_level.low";
			case AVERAGE: return "accounting.operation_level.average";
			case HIGH: return "accounting.operation_level.high";
			case SUSPENDED: return "accounting.operation_level.suspended";
			default:
					return "accounting.operation_level.lowest";
		}
	}

}
