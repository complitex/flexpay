package org.flexpay.eirc.sp;

public class SpFileFormatException extends Exception {
	private Long position;

	public SpFileFormatException(String s) {
		super(s);
	}

	public SpFileFormatException(String s, Long position) {
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
