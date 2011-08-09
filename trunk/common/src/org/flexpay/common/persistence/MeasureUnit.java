package org.flexpay.common.persistence;

import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class MeasureUnit extends DomainObjectWithStatus {

	private Set<MeasureUnitName> unitNames = set();

	/**
	 * Constructs a new DomainObject.
	 */
	public MeasureUnit() {
	}

	public MeasureUnit(@NotNull Long id) {
		super(id);
	}

	public MeasureUnit(@NotNull Stub<MeasureUnit> stub) {
		super(stub.getId());
	}

	public Set<MeasureUnitName> getUnitNames() {
		return unitNames;
	}

	public void setUnitNames(Set<MeasureUnitName> unitNames) {
		this.unitNames = unitNames;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof MeasureUnit && super.equals(o);
	}

	public void setName(MeasureUnitName unitName) {
		unitNames = TranslationUtil.setTranslation(unitNames, this, unitName);
	}

	/**
	 * Get name translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public MeasureUnitName getTranslation(@NotNull Language lang) {

		for (MeasureUnitName translation : getUnitNames()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}

	/**
	 * Get unit translation in a default language
	 *
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public MeasureUnitName getDefaultTranslation() {
		return getTranslation(ApplicationConfig.getDefaultLanguage());
	}

}
