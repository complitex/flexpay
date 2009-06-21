package org.flexpay.payments.service.importexport.imp;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;

public class ClassToTypeRegistryPayments implements ClassToTypeRegistry {

	private static final int MODULE_BASE = 0x3000;

	public int getType(Class<? extends DomainObject> clazz) {

		if (Service.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x0201;
		}
		if (ServiceType.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x002;
		}
		if (Cashbox.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x006;
		}

		throw new IllegalArgumentException("Class " + clazz + " has no assigned type");
	}
}
