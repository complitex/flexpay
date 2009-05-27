package org.flexpay.common.persistence.history;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Collections;
import java.util.Set;

/**
 * Chunk of a history records recieved from external source.
 */
public class ExternalHistoryPack extends DomainObject {

	private Date receiveDate = new Date();
	private String sourceInstanceId;
	private Long consumptionGroupId;
	private FPFile file;

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

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("receiveDate", receiveDate).
				append("sourceInstanceId", sourceInstanceId).
				append("consumptionGroupId", consumptionGroupId).
				append("file-id", file.getId()).
				toString();
	}
}
