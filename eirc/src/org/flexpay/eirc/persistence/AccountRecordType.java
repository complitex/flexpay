package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;

/**
 * Personal account record types
 */
public class AccountRecordType extends DomainObject {

	public static int TYPE_UNKNOWN = 0;
	public static int TYPE_PAYMENT = 1;
	public static int TYPE_SERVICE_FEE = 2;
	public static int TYPE_BALANCE_CORRECTION = 3;
	public static int TYPE_CORRECTION = 4;

	private int typeId;
	private String description;

	/**
	 * Constructs a new DomainObject.
	 */
	public AccountRecordType() {
	}

	public AccountRecordType(Long id) {
		super(id);
	}

	/**
	 * Getter for property 'typeId'.
	 *
	 * @return Value for property 'typeId'.
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * Setter for property 'typeId'.
	 *
	 * @param typeId Value to set for property 'typeId'.
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * Getter for property 'description'.
	 *
	 * @return Value for property 'description'.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for property 'description'.
	 *
	 * @param description Value to set for property 'description'.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
