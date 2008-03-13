package org.flexpay.eirc.persistence.exchange;

import java.util.List;

public abstract class AbstractChangePersonalAccountOperation extends Operation {

	protected String oldValue;
	protected String newValue;

	protected AbstraсtChangePersonalAccountOperation(List<String> datum)
		throws InvalidContainerException {

		super(Integer.valueOf(datum.get(0)));
		if (datum.size() < 3) {
			throw new InvalidContainerException("Invalid change personal account operation data");
		}

		oldValue = datum.get(1);
		newValue = datum.get(2);
	}
}
