package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;

public class UnsupportedRegistryTypeException extends FlexPayException {
	public UnsupportedRegistryTypeException(String msg) {
		super(msg);
	}
}
