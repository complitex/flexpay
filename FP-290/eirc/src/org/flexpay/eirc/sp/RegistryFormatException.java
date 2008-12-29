package org.flexpay.eirc.sp;

import org.flexpay.common.exception.FlexPayException;

public class RegistryFormatException extends FlexPayException {
	private Long position;

	public RegistryFormatException(String s) {
		super(s);
	}

	public RegistryFormatException(String s, Long position) {
		super(s);
		this.position = position;
	}

	/**
	 * @return the position
	 */
	public long getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(long position) {
		this.position = position;
	}
}
