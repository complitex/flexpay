package org.flexpay.ab.service.impl;

import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.springframework.beans.factory.annotation.Required;

public class ObjectsFactoryProxy implements ObjectsFactory {

	private ObjectsFactoryHolder factoryHolder;

	/**
	 * Create a new Apartment
	 *
	 * @return new apartment instance
	 */
	public Apartment newApartment() {
		return factoryHolder.getInstance().newApartment();
	}

	/**
	 * Create a new Building
	 *
	 * @return new building instance
	 */
	public Building newBuilding() {
		return factoryHolder.getInstance().newBuilding();
	}

	@Required
	public void setFactoryHolder(ObjectsFactoryHolder factoryHolder) {
		this.factoryHolder = factoryHolder;
	}
}
