package org.flexpay.eirc.persistence.exchange;

import java.util.List;

/**
 * Set total live square of apartment
 */
public abstract class SetLiveSquareOperation extends AbstraсtChangePersonalAccountOperation {

	public SetLiveSquareOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}
}
