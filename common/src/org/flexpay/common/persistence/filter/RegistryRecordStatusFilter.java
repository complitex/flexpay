package org.flexpay.common.persistence.filter;

import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;

import java.util.Map;
import java.util.TreeMap;

public class RegistryRecordStatusFilter extends ObjectFilter {

	public static final Integer TYPE_ALL = -1;
	private static final Map<Integer, String> statusTypes = new TreeMap<Integer, String>();

	static {
		statusTypes.put(TYPE_ALL, "eirc.registry.record.status");
		statusTypes.put(RegistryRecordStatus.LOADED, "eirc.registry.record.status.LOADED");
		statusTypes.put(RegistryRecordStatus.PROCESSED_WITH_ERROR, "eirc.registry.record.status.PROCESSED_WITH_ERROR");
		statusTypes.put(RegistryRecordStatus.FIXED, "eirc.registry.record.status.FIXED");
		statusTypes.put(RegistryRecordStatus.PROCESSED, "eirc.registry.record.status.PROCESSED");
	}

	private Integer selectedStatus = TYPE_ALL;

	public static Map<Integer, String> getStatusTypes() {
		return statusTypes;
	}

	public Integer getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(Integer selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	/**
	 * Check if filter selectedStatus should be applyed when fetching
	 *
	 * @return <code>true</code> if selectedStatus != {@link #TYPE_ALL}, or <code>false</code> otherwise
	 */
	public boolean needFilter() {
		return !selectedStatus.equals(TYPE_ALL);
	}
}
