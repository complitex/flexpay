package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;

public class InvalidContainerException extends FlexPayException {

	public InvalidContainerException(String s) {
		super(s);
	}

	public InvalidContainerException(String s, Exception e) {
		super(s, e);
	}
}
