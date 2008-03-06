package org.flexpay.eirc.service.importexport.imp;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.eirc.persistence.PersonalAccount;

public class ClassToTypeRegistry extends org.flexpay.ab.service.importexport.imp.ClassToTypeRegistry {

	public int getType(Class<? extends DomainObject> clazz) {
		if (PersonalAccount.class.isAssignableFrom(clazz)) {
			return 0x0101;
		}

		return super.getType(clazz);
	}
}
