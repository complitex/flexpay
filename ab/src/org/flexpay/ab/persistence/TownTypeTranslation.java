package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.LangNameTranslation;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;

/**
 * TownTypeTranslation is a trnaslation of TownType to particular language
 */
@Entity
@Table (name = "town_type_translations_tbl", uniqueConstraints = {
@UniqueConstraint (columnNames = {"language_id", "town_type_id"})
		})
public class TownTypeTranslation implements java.io.Serializable {

	private Long id;
	private String name;
	private Language language;
	private TownType townType;

	private LangNameTranslation translation;

	/** Constructs a new TownTypeTranslation. */
	public TownTypeTranslation() {
	}

	/**
	 * Getter for property 'id'.
	 *
	 * @return Value for property 'id'.
	 */
	@Id
	@GeneratedValue
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
	@Basic
	@Column (nullable = false, length = 120)
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
	@ManyToOne
	@JoinColumn (name = "language_id", nullable = false)
	public Language getLanguage() {
		return this.language;
	}

	/**
	 * Setter for property 'language'.
	 *
	 * @param language Value to set for property 'language'.
	 */
	public void setLanguage(Language language) {
		this.language = language;
	}

	/**
	 * Getter for property 'townType'.
	 *
	 * @return Value for property 'townType'.
	 */
	@ManyToOne
	@JoinColumn (name = "town_type_id", nullable = false)
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
	@Transient
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
				.append("Language", language.getLangIsoCode())
				.append("Name", name)
				.toString();
	}
}
