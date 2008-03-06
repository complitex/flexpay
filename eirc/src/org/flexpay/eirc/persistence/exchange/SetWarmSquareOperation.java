package org.flexpay.eirc.persistence.exchange;

import java.util.List;

/**
 * Open new service provider personal account
 */
public abstract class SetWarmSquareOperation extends AbstraсtChangePersonalAccountOperation {

	public SetWarmSquareOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}
}