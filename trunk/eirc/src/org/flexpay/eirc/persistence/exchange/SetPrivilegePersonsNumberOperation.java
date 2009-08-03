package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;

import java.util.List;

public class SetPrivilegePersonsNumberOperation extends AbstractChangePersonalAccountOperation {

	public SetPrivilegePersonsNumberOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}

	public DelayedUpdate process(Registry registry, RegistryRecord record) throws FlexPayException {
		return DelayedUpdateNope.INSTANCE;
	}

}
