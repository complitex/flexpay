package org.flexpay.eirc.persistence.exchange;

import java.util.List;

/**
 * Open new service provider personal account
 */
public abstract class SetNumberOfHabitantsOperation extends AbstraсtChangePersonalAccountOperation {

	public SetNumberOfHabitantsOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}
}