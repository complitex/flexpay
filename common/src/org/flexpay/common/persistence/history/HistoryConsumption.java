package org.flexpay.common.persistence.history;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObject;

/**
 * Single history record consumption
 */
public class HistoryConsumption extends DomainObject {

	private HistoryRecord historyRecord;
	private HistoryConsumptionGroup group;

	public HistoryRecord getHistoryRecord() {
		return historyRecord;
	}

	public void setHistoryRecord(HistoryRecord historyRecord) {
		this.historyRecord = historyRecord;
	}

	public HistoryConsumptionGroup getGroup() {
		return group;
	}

	public void setGroup(HistoryConsumptionGroup group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("id", getId()).
				append("record", historyRecord.getId()).
				append("group", group.getId()).
				toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof HistoryConsumption && super.equals(obj);
	}
}
