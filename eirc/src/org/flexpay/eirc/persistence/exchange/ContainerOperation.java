package org.flexpay.eirc.persistence.exchange;

public abstract class ContainerOperation extends Operation {

	private int typeId;

	protected ContainerOperation(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * Getter for property 'type'.
	 *
	 * @return Value for property 'type'.
	 */
	public int getTypeId() {
		return typeId;
	}

}
