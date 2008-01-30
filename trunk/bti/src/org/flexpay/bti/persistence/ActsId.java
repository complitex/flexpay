package org.flexpay.ab.persistence.temp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * ActsId generated by hbm2java
 */
@Embeddable
public class ActsId implements Serializable {

	// Fields

	private int apartmentRelationId;
	private int actId;

	// Constructors

	/**
	 * default constructor
	 */
	public ActsId() {
	}

	/**
	 * full constructor
	 */
	public ActsId(int apartmentRelationId, int actId) {
		this.apartmentRelationId = apartmentRelationId;
		this.actId = actId;
	}

	// Property accessors

	@Column (name = "Apartment_Relation_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public int getApartmentRelationId() {
		return this.apartmentRelationId;
	}

	public void setApartmentRelationId(int apartmentRelationId) {
		this.apartmentRelationId = apartmentRelationId;
	}

	@Column (name = "Act_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public int getActId() {
		return this.actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}

}


