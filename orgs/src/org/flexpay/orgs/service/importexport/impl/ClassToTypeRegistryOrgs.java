package org.flexpay.orgs.service.importexport.impl;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.orgs.persistence.*;

public class ClassToTypeRegistryOrgs implements ClassToTypeRegistry {

	private static final int MODULE_BASE = 0x4000;

	@Override
	public int getType(Class<? extends DomainObject> clazz) {

		if (Organization.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x001;
		}
		if (Bank.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x002;
		}
		if (ServiceProvider.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x003;
		}
		if (ServiceOrganization.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x004;
		}
		if (PaymentCollector.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x005;
		}
		if (PaymentPoint.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x006;
		}
		if (Subdivision.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x007;
		}
		if (Cashbox.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x008;
		}

		throw new IllegalArgumentException("Class " + clazz + " has no assigned type");
	}
}
