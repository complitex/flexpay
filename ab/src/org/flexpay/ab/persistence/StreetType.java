package org.flexpay.ab.persistence;
// Generated 15.11.2007 14:59:52 by Hibernate Tools 3.2.0.b11


import java.util.Collection;
import java.util.Collections;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * StreetType entity class holds a general representation of various types of streets.
 */
@Entity
@Table(name="street_types_tbl")
@NamedQueries ({
@NamedQuery (name = "StreetType.listStreetTypes", query = "FROM StreetType")
		})
public class StreetType  implements java.io.Serializable {

	/**
	 * Active street type status
	 */
	public static final int STATUS_ACTIVE = 0;

	/**
	 * Disabled street type status
	 */
	public static final int STATUS_DISABLED = 1;

	private Long id;
	private int status;
	private Collection<StreetTypeTranslation> translations = Collections.emptyList();

	/**
	 * Constructs a new StreetType.
	 */
	public StreetType() {
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
	 * Getter for property 'translations'.
	 *
	 * @return Value for property 'translations'.
	 */
	public Collection<StreetTypeTranslation> getTranslations() {
		return translations;
	}

	/**
	 * Setter for property 'translations'.
	 *
	 * @param translations Value to set for property 'typeTranslations'.
	 */
	public void setTranslations(Collection<StreetTypeTranslation> translations) {
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


