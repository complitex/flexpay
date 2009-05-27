package org.flexpay.common.persistence.history;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

/**
 * Holder of a history unpacker data, used to ensure history records received are stored in the same order they were
 * created.
 */
public class HistoryUnPackData extends DomainObject {

	private String sourceInstanceId;
	private ExternalHistoryPack lastUnPackedPacket;

	/**
	 * Constructs a new DomainObject.
	 */
	public HistoryUnPackData() {
	}

	public HistoryUnPackData(@NotNull Long id) {
		super(id);
	}

	public HistoryUnPackData(@NotNull Stub<HistoryUnPackData> stub) {
		super(stub.getId());
	}

	public String getSourceInstanceId() {
		return sourceInstanceId;
	}

	public void setSourceInstanceId(String sourceInstanceId) {
		this.sourceInstanceId = sourceInstanceId;
	}

	public ExternalHistoryPack getLastUnPackedPacket() {
		return lastUnPackedPacket;
	}

	public void setLastUnPackedPacket(ExternalHistoryPack lastUnPackedPacket) {
		this.lastUnPackedPacket = lastUnPackedPacket;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("sourceInstanceId", sourceInstanceId).
				append("lastUnPackedPacket-id", lastUnPackedPacket != null ? lastUnPackedPacket.getId() : null).
				toString();
	}
}
