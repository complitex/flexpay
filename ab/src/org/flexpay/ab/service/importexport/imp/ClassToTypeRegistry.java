package org.flexpay.ab.service.importexport.imp;

import org.flexpay.ab.persistence.*;

public class ClassToTypeRegistry implements org.flexpay.common.service.importexport.ClassToTypeRegistry {

	public int getType(Class<?> clazz) {
		if (Person.class.isAssignableFrom(clazz)) {
			return 0x01;
		}
		if (Apartment.class.isAssignableFrom(clazz)) {
			return 0x06;
		}
		if (Buildings.class.isAssignableFrom(clazz)) {
			return 0x05;
		}
		if (Street.class.isAssignableFrom(clazz)) {
			return 0x04;
		}
		if (District.class.isAssignableFrom(clazz)) {
			return 0x03;
		}
		if (Town.class.isAssignableFrom(clazz)) {
			return 0x02;
		}
		if (Region.class.isAssignableFrom(clazz)) {
			return 0x02;
		}
		if (Country.class.isAssignableFrom(clazz)) {
			return 0x01;
		}

		throw new IllegalArgumentException("Class " + clazz + " has no assigned type");
	}
}
