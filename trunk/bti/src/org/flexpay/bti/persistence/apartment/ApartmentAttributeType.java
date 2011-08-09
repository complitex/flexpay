package org.flexpay.bti.persistence.apartment;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

/**
 * Simple apartment attribute type
 */
public abstract class ApartmentAttributeType extends DomainObjectWithStatus {

	private int isTemporal;
	private String uniqueCode;
	private ApartmentAttributeGroup group;
	private Set<ApartmentAttributeTypeName> translations = set();

	public ApartmentAttributeType() {
	}

	public ApartmentAttributeType(@NotNull Long id) {
		super(id);
	}

	public ApartmentAttributeType(@NotNull Stub<ApartmentAttributeType> stub) {
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

	public ApartmentAttributeGroup getGroup() {
		return group;
	}

	public void setGroup(ApartmentAttributeGroup group) {
		this.group = group;
	}

	public Set<ApartmentAttributeTypeName> getTranslations() {
		return translations;
	}

	@SuppressWarnings ({"UnusedDeclaration"})
	private void setTranslations(Set<ApartmentAttributeTypeName> translations) {
		this.translations = translations;
	}

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	/**
	 * Perform attribute validation
	 *
	 * @param value Attribute value to validate
	 * @throws org.flexpay.common.exception.FlexPayException if validation fails
	 */
	abstract public void validate(String value) throws FlexPayException;

	/**
	 * Get type name code
	 *
	 * @return type name code
	 */
	abstract public String getI18nTitle();

	public void setTranslation(ApartmentAttributeTypeName name) {
		translations = TranslationUtil.setTranslation(translations, this, name);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				append("isTemporal", isTemporal).
				append("uniqueCode", uniqueCode).
				toString();
	}

}
