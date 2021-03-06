package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SetPrivilegeTypeOperation extends AbstractChangePersonalAccountOperation {

	public SetPrivilegeTypeOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}

	public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException {
		return DelayedUpdateNope.INSTANCE;
	}

}
