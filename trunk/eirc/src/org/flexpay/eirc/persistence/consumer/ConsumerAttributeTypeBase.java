package org.flexpay.eirc.persistence.consumer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

public abstract class ConsumerAttributeTypeBase extends DomainObjectWithStatus {

	private int isTemporal;
	private String uniqueCode;
	// one of ValueObject.TYPE_XXX constants
	private int valueType;
	// optional measure unit
	private MeasureUnit measureUnit;
	private Set<ConsumerAttributeTypeName> names = Collections.emptySet();

	public ConsumerAttributeTypeBase() {
	}

	public ConsumerAttributeTypeBase(@NotNull Long id) {
		super(id);
	}

	public ConsumerAttributeTypeBase(Stub<ConsumerAttributeTypeBase> stub) {
		super(stub.getId());
	}

	/**
	 * Check if this attribute type is for temporal attributes
	 *
	 * @return <code>true</code> if type is temporal, or <code>false</code> otherwise
	 */
	public boolean isTemp() {
		return isTemporal != 0;
	}

	public void setTemp(boolean tmp) {
		isTemporal = tmp ? 1 : 0;
	}

	public int getTemporal() {
		return isTemporal;
	}

	public void setTemporal(int temporal) {
		isTemporal = temporal;
	}

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public Set<ConsumerAttributeTypeName> getNames() {
		return names;
	}

	private void setNames(Set<ConsumerAttributeTypeName> names) {
		this.names = names;
	}

	public void setTranslation(ConsumerAttributeTypeName name) {
		names = TranslationUtil.setTranslation(names, this, name);
	}

	public MeasureUnit getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(MeasureUnit measureUnit) {
		this.measureUnit = measureUnit;
	}

	public int getValueType() {
		return valueType;
	}

	public void setValueType(int valueType) {
		this.valueType = valueType;
	}

	/**
	 * Get type translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public ConsumerAttributeTypeName getTranslation(@NotNull Language lang) {

		for (ConsumerAttributeTypeName translation : names) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}

	/**
	 * Perform attribute validation
	 *
	 * @param value Attribute value to validate
	 * @throws org.flexpay.common.exception.FlexPayException if validation fails
	 */
	abstract public void validate(ConsumerAttribute value) throws FlexPayException;

	/**
	 * Get type name code
	 *
	 * @return type name code
	 */
	abstract public String getI18nTitle();

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ConsumerAttributeTypeBase && super.equals(obj);
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("id", id).
                append("status", status).
                append("isTemporal", isTemporal).
                append("uniqueCode", uniqueCode).
                append("valueType", valueType).
                append("measureUnit", measureUnit).
                toString();
    }
}
