package org.flexpay.eirc.persistence.exchange;

import java.util.List;

/**
 */
public abstract class SetPrivilegePersonOperation extends AbstraсtChangePersonalAccountOperation {

	public SetPrivilegePersonOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}
}