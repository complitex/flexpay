package org.flexpay.eirc.service;

public class InvalidRegistryTypeException extends Exception {

	public InvalidRegistryTypeException(int typeId) {
		super("Unsupported registry type #" + typeId);
	}
}
