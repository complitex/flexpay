package org.flexpay.common.persistence.history;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * Single object field modification record
 */
public class HistoryRecord extends DomainObject {

	private Integer oldIntValue;
	private Integer newIntValue;
	private Long oldLongValue;
	private Long newLongValue;
	private String oldStringValue;
	private String newStringValue;
	private Date oldDateValue;
	private Date newDateValue;
	private Double oldDoubleValue;
	private Double newDoubleValue;
	private int fieldType;
	private int operationOrder;
	private String language;
	private Date beginDate;
	private Date endDate;
	private String fieldKey;
	private int processingStatus;
	private Diff diff;

	/**
	 * Constructs a new DomainObject.
	 */
	public HistoryRecord() {
	}

	public HistoryRecord(@NotNull Long id) {
		super(id);
	}

	public HistoryRecord(@NotNull Stub<HistoryRecord> stub) {
		super(stub.getId());
	}

	public Integer getOldIntValue() {
		return oldIntValue;
	}

	public void setOldIntValue(Integer oldIntValue) {
		this.oldIntValue = oldIntValue;
	}

	public Integer getNewIntValue() {
		return newIntValue;
	}

	public void setNewIntValue(Integer newIntValue) {
		this.newIntValue = newIntValue;
	}

	public Long getOldLongValue() {
		return oldLongValue;
	}

	public void setOldLongValue(Long oldLongValue) {
		this.oldLongValue = oldLongValue;
	}

	public Long getNewLongValue() {
		return newLongValue;
	}

	public void setNewLongValue(Long newLongValue) {
		this.newLongValue = newLongValue;
	}

	public String getOldStringValue() {
		return oldStringValue;
	}

	public void setOldStringValue(String oldStringValue) {
		this.oldStringValue = oldStringValue;
	}

	public String getNewStringValue() {
		return newStringValue;
	}

	public void setNewStringValue(String newStringValue) {
		this.newStringValue = newStringValue;
	}

	public Date getOldDateValue() {
		return oldDateValue;
	}

	public void setOldDateValue(Date oldDateValue) {
		this.oldDateValue = oldDateValue;
	}

	public Date getNewDateValue() {
		return newDateValue;
	}

	public void setNewDateValue(Date newDateValue) {
		this.newDateValue = newDateValue;
	}

	public Double getOldDoubleValue() {
		return oldDoubleValue;
	}

	public void setOldDoubleValue(Double oldDoubleValue) {
		this.oldDoubleValue = oldDoubleValue;
	}

	public Double getNewDoubleValue() {
		return newDoubleValue;
	}

	public void setNewDoubleValue(Double newDoubleValue) {
		this.newDoubleValue = newDoubleValue;
	}

	public int getFieldType() {
		return fieldType;
	}

	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}

	public int getOperationOrder() {
		return operationOrder;
	}

	public void setOperationOrder(int operationOrder) {
		this.operationOrder = operationOrder;
	}

	public String getLanguage() {
		return language;
	}

	@Nullable
	public Language getLang() {
		for (Language lang : ApplicationConfig.getLanguages()) {
			if (lang.getLangIsoCode().equals(language)) {
				return lang;
			}
		}

		return null;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getFieldKey() {
		return fieldKey;
	}

	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}

	public int getProcessingStatus() {
		return processingStatus;
	}

	public void setProcessingStatus(int processingStatus) {
		this.processingStatus = processingStatus;
	}

	public Diff getDiff() {
		return diff;
	}

	public void setDiff(Diff diff) {
		this.diff = diff;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("id", getId()).
				append("oldIntValue", oldIntValue).
				append("newIntValue", newIntValue).
				append("oldLongValue", oldLongValue).
				append("newLongValue", newLongValue).
				append("oldStringValue", oldStringValue).
				append("newStringValue", newStringValue).
				append("oldDateValue", oldDateValue).
				append("newDateValue", newDateValue).
				append("oldDoubleValue", oldDoubleValue).
				append("newDoubleValue", newDoubleValue).
				append("fieldType", fieldType).
				append("operationOrder", operationOrder).
				append("language", language).
				append("beginDate", beginDate).
				append("endDate", endDate).
				append("fieldKey", fieldKey).
				append("processingStatus", processingStatus).
				append("diff", diff).
				toString();
	}
}
