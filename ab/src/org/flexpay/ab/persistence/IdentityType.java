package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

/**
 * IdentityType entity class holds a general representation of various types of identities.
 */
public class IdentityType extends DomainObjectWithStatus implements Comparable<IdentityType> {

	public static int TYPE_UNKNOWN = 0;
	public static int TYPE_FIO = 1;
	public static int TYPE_PASSPORT = 2;
	public static int TYPE_FOREIGN_PASSPORT = 3;

	public static String TYPE_NAME_UNKNOWN = "unknown";
	public static String TYPE_NAME_FIO = "fio";
	public static String TYPE_NAME_PASSPORT = "passport";
	public static String TYPE_NAME_FOREIGN_PASSPORT = "foreignPassport";

	private int typeId = TYPE_UNKNOWN;

	private Set<IdentityTypeTranslation> translations = Collections.emptySet();

	public IdentityType() {
	}

	public IdentityType(Long id) {
		super(id);
	}

	public IdentityType(Stub<IdentityType> stub) {
		super(stub.getId());
	}

	public Set<IdentityTypeTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<IdentityTypeTranslation> translations) {
		this.translations = translations;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public boolean isFIO() {
		return typeId == TYPE_FIO;
	}

	public void setTranslation(IdentityTypeTranslation translation) {
		translations = TranslationUtil.setTranslation(translations, this, translation);
	}

	/**
	 * Get type translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public IdentityTypeTranslation getTranslation(@NotNull Language lang) {
		for (IdentityTypeTranslation translation : getTranslations()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}

	/**
	 * Get type translation in a default language
	 *
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public IdentityTypeTranslation getDefaultTranslation() {
		return getTranslation(ApplicationConfig.getDefaultLanguage());
	}

	@Override
	public int compareTo(IdentityType o) {
		if (o == null || o.getId() == null) {
			return 1;
		}
		if (getId() == null) {
			return -1;
		}
		return getId().compareTo(o.getId());
	}

}
