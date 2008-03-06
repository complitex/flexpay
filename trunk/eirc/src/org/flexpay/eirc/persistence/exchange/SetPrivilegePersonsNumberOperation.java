package org.flexpay.eirc.persistence.exchange;

import java.util.List;

/**
 */
public abstract class SetPrivilegePersonsNumberOperation extends AbstraсtChangePersonalAccountOperation {

	public SetPrivilegePersonsNumberOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}
}