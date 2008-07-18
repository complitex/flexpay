package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * IdentityType entity class holds a general representation of various types of
 * identities.
 */
public class IdentityType extends DomainObjectWithStatus {

	public static int TYPE_UNKNOWN = 0;
	public static int TYPE_FIO = 1;
	public static int TYPE_PASSPORT = 2;
	public static int TYPE_FOREIGN_PASSPORT = 3;

	@NonNls
	public static String TYPE_NAME_UNKNOWN = "unknown";
	@NonNls
	public static String TYPE_NAME_FIO = "fio";
	@NonNls
	public static String TYPE_NAME_PASSPORT = "passport";
	@NonNls
	public static String TYPE_NAME_FOREIGN_PASSPORT = "foreignPassport";

	private int typeId = TYPE_UNKNOWN;

	private Collection<IdentityTypeTranslation> translations = Collections.emptyList();

	/**
	 * Constructs a new IdentityType.
	 */
	public IdentityType() {
	}

	public IdentityType(Long id) {
		super(id);
	}

	/**
	 * Getter for property 'translations'.
	 *
	 * @return Value for property 'translations'.
	 */
	public Collection<IdentityTypeTranslation> getTranslations() {
		return translations;
	}

	/**
	 * Setter for property 'translations'.
	 *
	 * @param translations Value to set for property 'translations'.
	 */
	public void setTranslations(Collection<IdentityTypeTranslation> translations) {
		this.translations = translations;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append("Id", getId())
				.append("Status", getStatus())
				.append("Translations", translations.toArray())
				.toString();
	}

	public boolean isFIO() {
		return typeId == TYPE_FIO;
	}

	public void setTranslation(@NotNull IdentityTypeTranslation translation) {
		boolean needRemove = false;
		for (IdentityTypeTranslation typeTranslation : translations) {
			if (typeTranslation.isSameLanguage(translation)) {
				if (StringUtils.isBlank(translation.getName())) {
					needRemove = true;
					translation = typeTranslation;
					break;
				}
				typeTranslation.setName(translation.getName());
				return;
			}
		}

		if (needRemove) {
			translations.remove(translation);
		} else {
			if (translations == Collections.EMPTY_SET) {
				translations = set();
			}

			translations.add(translation);
			translation.setTranslatable(this);
		}
	}
}
