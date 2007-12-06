package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.LangNameTranslation;
import org.flexpay.common.persistence.Language;

/**
 * TownTypeTranslation is a trnaslation of TownType to particular language
 */
public class TownTypeTranslation implements java.io.Serializable {

	private Long id;
	private String name;
	private Language lang;
	private TownType townType;

	private LangNameTranslation translation;

	/**
	 * Constructs a new TownTypeTranslation.
	 */
	public TownTypeTranslation() {
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
	 * Getter for property 'name'.
	 *
	 * @return Value for property 'name'.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter for property 'name'.
	 *
	 * @param name Value to set for property 'name'.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for property 'language'.
	 *
	 * @return Value for property 'language'.
	 */
	public Language getLang() {
		return this.lang;
	}

	/**
	 * Setter for property 'language'.
	 *
	 * @param lang Value to set for property 'language'.
	 */
	public void setLang(Language lang) {
		this.lang = lang;
	}

	/**
	 * Getter for property 'townType'.
	 *
	 * @return Value for property 'townType'.
	 */
	public TownType getTownType() {
		return townType;
	}

	/**
	 * Setter for property 'townType'.
	 *
	 * @param townType Value to set for property 'townType'.
	 */
	public void setTownType(TownType townType) {
		this.townType = townType;
	}

	/**
	 * Getter for property 'translation'.
	 *
	 * @return Value for property 'translation'.
	 */
	public LangNameTranslation getTranslation() {
		return translation;
	}

	/**
	 * Setter for property 'translation'.
	 *
	 * @param translation Value to set for property 'translation'.
	 */
	public void setTranslation(LangNameTranslation translation) {
		this.translation = translation;
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
				.append("Language", lang.getLangIsoCode())
				.append("Name", name)
				.toString();
	}
}
