package org.flexpay.common.service.importexport;

public class ImportOperationTypeHolder {

	private ImportOperationType type;

	/**
	 * Constructs a new ImportOperationTypeHolder.
	 */
	public ImportOperationTypeHolder() {
		type = ImportOperationType.unknown;
	}

	/**
	 * Constructs a new ImportOperationTypeHolder.
	 *
	 * @param type Operation type
	 */
	public ImportOperationTypeHolder(ImportOperationType type) {
		this.type = type;
	}

	/**
	 * Getter for property 'type'.
	 *
	 * @return Value for property 'type'.
	 */
	public ImportOperationType getType() {
		return type;
	}

	/**
	 * Setter for property 'type'.
	 *
	 * @param type Value to set for property 'type'.
	 */
	public void setType(ImportOperationType type) {
		this.type = type;
	}
}
