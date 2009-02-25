package org.flexpay.common.persistence.history;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObject;

/**
 * Single history record consumption
 */
public class HistoryConsumption extends DomainObject {

	private HistoryRecord historyRecord;
	private HistoryConsumer historyConsumer;

	public HistoryRecord getHistoryRecord() {
		return historyRecord;
	}

	public void setHistoryRecord(HistoryRecord historyRecord) {
		this.historyRecord = historyRecord;
	}

	public HistoryConsumer getHistoryConsumer() {
		return historyConsumer;
	}

	public void setHistoryConsumer(HistoryConsumer historyConsumer) {
		this.historyConsumer = historyConsumer;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("id", getId()).
				append("record", historyRecord.getId()).
				append("consumer", historyConsumer.getId()).
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