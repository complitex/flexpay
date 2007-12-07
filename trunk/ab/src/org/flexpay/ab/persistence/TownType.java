package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Collection;
import java.util.Collections;

/**
 * TownType entity class holds a general representation of various types of localities,
 * such as towns, villages, etc.
 */
public class TownType implements java.io.Serializable {

	/**
	 * Active town type staus
	 */
	public static final int STATUS_ACTIVE = 0;

	/**
	 * Disabled town type status
	 */
	public static final int STATUS_DISABLED = 1;

	private Long id;
	private int status;
	private Collection<TownTypeTranslation> translations = Collections.emptyList();

	/**
	 * Constructs a new TownType.
	 */
	public TownType() {
	}

	/**
	 * Getter for property 'id'.
	 *
	 * @return Value for property 'id'.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Setter for property 'id'.
	 *
	 * @param id Value to set for property 'id'.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter for property 'typeTranslations'.
	 *
	 * @return Value for property 'typeTranslations'.
	 */
	public Collection<TownTypeTranslation> getTranslations() {
		return translations;
	}

	/**
	 * Setter for property 'typeTranslations'.
	 *
	 * @param translations Value to set for property 'typeTranslations'.
	 */
	public void setTranslations(Collection<TownTypeTranslation> translations) {
		this.translations = translations;
	}

	/**
	 * Getter for property 'status'.
	 *
	 * @return Value for property 'status'.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Setter for property 'status'.
	 *
	 * @param status Value to set for property 'status'.
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Id", id)
				.append("Status", status)
				.append("Translations", translations.toArray())
				.toString();
	}
}
