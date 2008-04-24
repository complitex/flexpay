package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SpRegistryType extends DomainObject {
	public static final int TYPE_UNKNOWN = 0;
	public static final int TYPE_BALANCE = 1;
	public static final int TYPE_PAYMENT = 2;

	private int code;

	/**
	 * Constructs a new DomainObject.
	 */
	public SpRegistryType() {
	}

	public SpRegistryType(Long id) {
		super(id);
	}
	

	/**
	 * Getter for property 'code'.
	 *
	 * @return Value for property 'code'.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Setter for property 'typeId'.
	 *
	 * @param typeId Value to set for property 'typeId'.
	 */
	public void setCode(int typeId) {
		this.code = typeId;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("code: ", code)
				.toString();
	}
}
