package org.flexpay.common.persistence.history;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * Chunk of a history records received from external source.
 */
public class ExternalHistoryPack extends DomainObject {

	private Date receiveDate = new Date();
	private String sourceInstanceId;
	private Long consumptionGroupId;
	private FPFile file;
	private int unPackTries = 0;

	public ExternalHistoryPack() {
	}

	public ExternalHistoryPack(@NotNull Long id) {
		super(id);
	}

	public ExternalHistoryPack(@NotNull Stub<ExternalHistoryPack> stub) {
		super(stub.getId());
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getSourceInstanceId() {
		return sourceInstanceId;
	}

	public void setSourceInstanceId(String sourceInstanceId) {
		this.sourceInstanceId = sourceInstanceId;
	}

	public Long getConsumptionGroupId() {
		return consumptionGroupId;
	}

	public void setConsumptionGroupId(Long consumptionGroupId) {
		this.consumptionGroupId = consumptionGroupId;
	}

	public FPFile getFile() {
		return file;
	}

	public void setFile(FPFile file) {
		this.file = file;
	}

	public int getUnPackTries() {
		return unPackTries;
	}

	public void setUnPackTries(int unPackTries) {
		this.unPackTries = unPackTries;
	}

	public void incrementUnpackTries() {
		++unPackTries;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("receiveDate", receiveDate).
				append("sourceInstanceId", sourceInstanceId).
				append("consumptionGroupId", consumptionGroupId).
				append("file-id", file.getId()).
				append("upPackTries", unPackTries).
				toString();
	}
}
