package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.exception.FlexPayException;

import java.util.List;

public class SetPrivilegePersonsNumberOperation extends AbstractChangePersonalAccountOperation {

	public SetPrivilegePersonsNumberOperation(List<String> datum) throws InvalidContainerException {
		super(datum);
	}

	public void process(Registry registry, RegistryRecord record) throws FlexPayException {
	}

}
