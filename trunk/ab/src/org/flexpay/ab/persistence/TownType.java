package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * TownType entity class holds a general representation of various types of localities,
 * such as towns, villages, etc.
 */
@Entity
@Table (name = "town_types_tbl")
@NamedQueries ({
@NamedQuery (name = "TownType.listTownTypes", query = "FROM TownType")
		})
public class TownType implements java.io.Serializable {

	private Long id;
	private Collection<TownTypeTranslation> typeTranslations = Collections.emptyList();

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
	public Collection<TownTypeTranslation> getTypeTranslations() {
		return typeTranslations;
	}

	/**
	 * Setter for property 'typeTranslations'.
	 *
	 * @param typeTranslations Value to set for property 'typeTranslations'.
	 */
	public void setTypeTranslations(Collection<TownTypeTranslation> typeTranslations) {
		this.typeTranslations = typeTranslations;
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
				.append("Translations", typeTranslations.toArray())
				.toString();
	}
}
