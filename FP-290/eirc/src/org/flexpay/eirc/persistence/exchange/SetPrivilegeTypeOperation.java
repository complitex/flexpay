package org.flexpay.eirc.persistence.exchange;

import java.util.List;

/**
 */
public abstract class SetPrivilegeTypeOperation extends AbstractChangePersonalAccountOperation {

	public SetPrivilegeTypeOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}
}