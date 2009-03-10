package org.flexpay.common.persistence.history.impl;

public class ObjectsSyncerFactory {

	private ObjectsSyncerImpl objectsSyncer;

	public ObjectsSyncerImpl getInstance() {

		if (objectsSyncer == null) {
			objectsSyncer = new ObjectsSyncerImpl();
		}

		return objectsSyncer;
	}
}
