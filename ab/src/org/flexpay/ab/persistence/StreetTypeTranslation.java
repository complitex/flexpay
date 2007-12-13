package org.flexpay.ab.persistence;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * StreetTypeTranslation is a trnaslation of StreetType to particular language
 */
@Entity
@Table(name = "street_type_translations_tbl", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"language_id", "street_type_id" }) })
public class StreetTypeTranslation extends AbstractTranslation {

	private StreetType streetType;

	/** Constructs a new StreetTypeTranslation. */
	public StreetTypeTranslation() {
		super();
	}

	/**
	 * Getter for property 'streetType'.
	 * 
	 * @return Value for property 'streetType'.
	 */
	public StreetType getStreetType() {
		return streetType;
	}

	/**
	 * Setter for property 'streetType'.
	 * 
	 * @param townType
	 *            Value to set for property 'streetType'.
	 */
	public void setStreetType(StreetType streetType) {
		this.streetType = streetType;
	}
}
