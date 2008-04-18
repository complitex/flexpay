package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SpRegistryType extends DomainObject {
	public static final int TYPE_UNKNOWN = 0;
	public static final int TYPE_BALANCE = 1;
	public static final int TYPE_PAYMENT = 2;

	private String name;
	private String direction;
	private int typeId;

	/**
	 * Constructs a new DomainObject.
	 */
	public SpRegistryType() {
	}

	public SpRegistryType(Long id) {
		super(id);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}
	/**
	 * @param direction the direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
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

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.append("name", name)
				.append("typeId", typeId)
				.toString();
	}
}
