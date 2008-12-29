package org.flexpay.eirc.persistence.exchange;

import java.util.List;

/**
 * Open new service provider personal account
 */
public abstract class SetTotalSquareOperation extends AbstractChangePersonalAccountOperation {

	public SetTotalSquareOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}
}