package org.flexpay.eirc.persistence.exchange;

import java.util.List;

/**
 * Set total live square of apartment
 */
public abstract class SetLiveSquareOperation extends AbstractChangePersonalAccountOperation {

	public SetLiveSquareOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}
}
