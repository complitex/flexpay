package org.flexpay.eirc.persistence.exchange;

import java.util.List;

/**
 */
public abstract class SetResponsiblePersonOperation extends AbstractChangePersonalAccountOperation {

	public SetResponsiblePersonOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}
}