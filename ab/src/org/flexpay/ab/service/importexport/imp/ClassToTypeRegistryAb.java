package org.flexpay.ab.service.importexport.imp;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;

public class ClassToTypeRegistryAb implements ClassToTypeRegistry {

	private static final int MODULE_BASE = 0x1000;

	@Override
	public int getType(Class<? extends DomainObject> clazz) {
		if (Person.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x09;
		}
		if (Apartment.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x08;
		}
		if (BuildingAddress.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x07;
		}
		if (Building.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x0A;
		}
		if (AddressAttributeType.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x0B;
		}
		if (Street.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x06;
		}
		if (District.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x05;
		}
		if (StreetType.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x04;
		}
		if (Town.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x03;
		}
		if (TownType.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x10;
		}
		if (Region.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x02;
		}
		if (Country.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x01;
		}
		if (IdentityType.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x0C;
		}

		throw new IllegalArgumentException("Class " + clazz + " has no assigned type");
	}
}
