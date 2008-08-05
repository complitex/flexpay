package org.flexpay.eirc.persistence.filters;

import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.eirc.persistence.SpRegistryRecordStatus;

import java.util.Map;
import java.util.TreeMap;

public class RegistryRecordStatusFilter extends ObjectFilter {

	public static final Integer TYPE_ALL = -1;
	private static final Map<Integer, String> statusTypes = new TreeMap<Integer, String>();

	static {
		statusTypes.put(TYPE_ALL, "eirc.registry.record.status");
		statusTypes.put(SpRegistryRecordStatus.LOADED, "eirc.registry.record.status.LOADED");
		statusTypes.put(SpRegistryRecordStatus.PROCESSED_WITH_ERROR, "eirc.registry.record.status.PROCESSED_WITH_ERROR");
		statusTypes.put(SpRegistryRecordStatus.FIXED, "eirc.registry.record.status.FIXED");
		statusTypes.put(SpRegistryRecordStatus.PROCESSED, "eirc.registry.record.status.PROCESSED");
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
