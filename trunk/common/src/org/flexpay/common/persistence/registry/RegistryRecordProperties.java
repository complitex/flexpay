package org.flexpay.common.persistence.registry;

import org.flexpay.common.persistence.DomainObject;

/**
 * Base class for registry record properties
 */
public class RegistryRecordProperties extends DomainObject {

	private RegistryRecord record;

	public RegistryRecord getRecord() {
		return record;
	}

	public void setRecord(RegistryRecord record) {
		this.record = record;
	}
}
