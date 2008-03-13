package org.flexpay.eirc.persistence.exchange;

import java.util.List;

/**
 * Open new service provider personal account
 */
public abstract class OpenAccountOperation extends AbstractChangePersonalAccountOperation {

	public OpenAccountOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}


}
