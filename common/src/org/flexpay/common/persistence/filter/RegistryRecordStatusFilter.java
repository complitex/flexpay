package org.flexpay.common.persistence.filter;

import org.flexpay.common.persistence.registry.RegistryRecordStatus;

import java.util.List;

public class RegistryRecordStatusFilter extends PrimaryKeyFilter<RegistryRecordStatus> {

	private List<RegistryRecordStatus> recordStatuses;

	public List<RegistryRecordStatus> getRecordStatuses() {
		return recordStatuses;
	}

	public void setRecordStatuses(List<RegistryRecordStatus> recordStatuses) {
		this.recordStatuses = recordStatuses;
	}
}
