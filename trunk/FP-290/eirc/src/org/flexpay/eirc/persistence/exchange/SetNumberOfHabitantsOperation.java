package org.flexpay.eirc.persistence.exchange;

import java.util.List;

/**
 * Open new service provider personal account
 */
public abstract class SetNumberOfHabitantsOperation extends AbstractChangePersonalAccountOperation {

	public SetNumberOfHabitantsOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}
}