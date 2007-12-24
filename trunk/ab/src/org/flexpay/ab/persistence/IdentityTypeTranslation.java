package org.flexpay.ab.persistence;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * IdentityTypeTranslation is a translation of IdentityType to particular language
 */
@Entity
@Table(name = "identity_type_translations_tbl", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"language_id", "identity_type_id" }) })
public class IdentityTypeTranslation extends AbstractTranslation {

	private IdentityType identityType;

	/** Constructs a new IdentityTypeTranslation. */
	public IdentityTypeTranslation() {
		super();
	}

	/**
	 * Getter for property 'identityType'.
	 * 
	 * @return Value for property 'identityType'.
	 */
	public IdentityType getIdentityType() {
		return identityType;
	}

	/**
	 * Setter for property 'identityType'.
	 * 
	 * @param identityType
	 *            Value to set for property 'identityType'.
	 */
	public void setIdentityType(IdentityType identityType) {
		this.identityType = identityType;
	}
}

