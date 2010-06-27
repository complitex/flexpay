package org.flexpay.eirc.persistence.registry;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.registry.Registry;

public class ProcessRegistryVariableInstance extends DomainObject {

	private Long processId;
	private Long charPoint;
	private Long lastProcessedRegistryRecord;
	private Registry registry;
	private Integer processedCountRecords;
	private Integer processedCountLines;

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public Long getCharPoint() {
		return charPoint;
	}

	public void setCharPoint(Long charPoint) {
		this.charPoint = charPoint;
	}

	public Long getLastProcessedRegistryRecord() {
		return lastProcessedRegistryRecord;
	}

	public void setLastProcessedRegistryRecord(Long lastProcessedRegistryRecord) {
		this.lastProcessedRegistryRecord = lastProcessedRegistryRecord;
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public Integer getProcessedCountRecords() {
		return processedCountRecords;
	}

	public void setProcessedCountRecords(Integer processedCountRecords) {
		this.processedCountRecords = processedCountRecords;
	}

	public Integer getProcessedCountLines() {
		return processedCountLines;
	}

	public void setProcessedCountLines(Integer processedCountLines) {
		this.processedCountLines = processedCountLines;
	}
}
