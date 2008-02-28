package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;

public class SpRegistryType extends DomainObject {
	public static final String DIRECTION_IN = "in";
	public static final String DIRECTION_OUT = "out";
	public static final String DIRECTION_IN_OUT = "in_out";
	
	private String name;
	private String direction;
	
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
	
	
}
