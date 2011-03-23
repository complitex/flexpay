package org.flexpay.common.persistence.history;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Single object field modification record
 */
public class HistoryRecord extends DomainObject {

	private Boolean oldBoolValue;
	private Boolean newBoolValue;
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
	// decimals scale is 5
	private BigDecimal oldDecimalValue;
	private BigDecimal newDecimalValue;
	private int fieldType;
	private int operationOrder;
	private String language;
	private Date beginDate;
	private Date endDate;
	private String fieldKey;
	private String fieldKey2;
	private String fieldKey3;
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

	@Nullable
	public Boolean getOldBoolValue() {
		return oldBoolValue;
	}

	public void setOldBoolValue(Boolean oldBoolValue) {
		this.oldBoolValue = oldBoolValue;
	}

	@Nullable
	public Boolean getNewBoolValue() {
		return newBoolValue;
	}

	public void setNewBoolValue(Boolean newBoolValue) {
		this.newBoolValue = newBoolValue;
	}

	@Nullable
	public Integer getOldIntValue() {
		return oldIntValue;
	}

	public void setOldIntValue(Integer oldIntValue) {
		this.oldIntValue = oldIntValue;
	}

	@Nullable
	public Integer getNewIntValue() {
		return newIntValue;
	}

	public void setNewIntValue(Integer newIntValue) {
		this.newIntValue = newIntValue;
	}

	@NotNull
	public Integer getNewIntValueNotNull() {
		return newIntValue;
	}

	@Nullable
	public Long getOldLongValue() {
		return oldLongValue;
	}

	public void setOldLongValue(Long oldLongValue) {
		this.oldLongValue = oldLongValue;
	}

	@Nullable
	public Long getNewLongValue() {
		return newLongValue;
	}

	public void setNewLongValue(Long newLongValue) {
		this.newLongValue = newLongValue;
	}

	@Nullable
	public String getOldStringValue() {
		return oldStringValue;
	}

	public void setOldStringValue(String oldStringValue) {
		this.oldStringValue = oldStringValue;
	}

	@Nullable
	public String getNewStringValue() {
		return newStringValue;
	}

	@NotNull
	public String getNewStringValueNotNull() {
		return newStringValue == null ? "" : newStringValue;
	}

	public void setNewStringValue(String newStringValue) {
		this.newStringValue = newStringValue;
	}

	@Nullable
	public Date getOldDateValue() {
		return oldDateValue;
	}

	public void setOldDateValue(Date oldDateValue) {
		this.oldDateValue = oldDateValue;
	}

	@Nullable
	public Date getNewDateValue() {
		return newDateValue;
	}

	public void setNewDateValue(Date newDateValue) {
		this.newDateValue = newDateValue;
	}

	@Nullable
	public Double getOldDoubleValue() {
		return oldDoubleValue;
	}

	public void setOldDoubleValue(Double oldDoubleValue) {
		this.oldDoubleValue = oldDoubleValue;
	}

	@Nullable
	public Double getNewDoubleValue() {
		return newDoubleValue;
	}

	public void setNewDoubleValue(Double newDoubleValue) {
		this.newDoubleValue = newDoubleValue;
	}

	@Nullable
	public BigDecimal getOldDecimalValue() {
		return oldDecimalValue;
	}

	public void setOldDecimalValue(BigDecimal oldDecimalValue) {
		this.oldDecimalValue = oldDecimalValue;
	}

	@Nullable
	public BigDecimal getNewDecimalValue() {
		return newDecimalValue;
	}

	public void setNewDecimalValue(BigDecimal newDecimalValue) {
		this.newDecimalValue = newDecimalValue;
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

	@Nullable
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

	@Nullable
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	@Nullable
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Nullable
	public String getFieldKey() {
		return fieldKey;
	}

	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}

	@Nullable
	public String getFieldKey2() {
		return fieldKey2;
	}

	public void setFieldKey2(String fieldKey2) {
		this.fieldKey2 = fieldKey2;
	}

	@Nullable
	public String getFieldKey3() {
		return fieldKey3;
	}

	public void setFieldKey3(String fieldKey3) {
		this.fieldKey3 = fieldKey3;
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
				append("oldBoolValue", oldBoolValue).
				append("newBoolValue", newBoolValue).
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
				append("fieldKey2", fieldKey2).
				append("fieldKey3", fieldKey3).
				append("processingStatus", processingStatus).
				append("diff", diff).
				toString();
	}
}
