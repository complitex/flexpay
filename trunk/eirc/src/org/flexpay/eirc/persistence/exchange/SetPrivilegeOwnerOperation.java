package org.flexpay.eirc.persistence.exchange;

import java.util.List;

/**
 * Open new service provider personal account
 */
public abstract class SetPrivilegeOwnerOperation extends AbstraсtChangePersonalAccountOperation {

	public SetPrivilegeOwnerOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}
}