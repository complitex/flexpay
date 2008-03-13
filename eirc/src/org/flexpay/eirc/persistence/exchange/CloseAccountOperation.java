package org.flexpay.eirc.persistence.exchange;

import java.util.List;

/**
 * Close service provider personal account
 */
public abstract class CloseAccountOperation extends AbstractChangePersonalAccountOperation {

	public CloseAccountOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}
}