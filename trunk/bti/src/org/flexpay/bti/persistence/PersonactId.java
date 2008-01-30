package org.flexpay.bti.persistence;
// Generated 15.11.2007 14:59:52 by Hibernate Tools 3.2.0.b11


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * PersonactId generated by hbm2java
 */
@Embeddable
public class PersonactId implements java.io.Serializable {


	private int id;
	private int actId;
	private int personId;

	public PersonactId() {
	}

	public PersonactId(int id, int actId, int personId) {
		this.id = id;
		this.actId = actId;
		this.personId = personId;
	}


	@Column (name = "ID", nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column (name = "Act_ID", nullable = false)
	public int getActId() {
		return this.actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}

	@Column (name = "Person_ID", nullable = false)
	public int getPersonId() {
		return this.personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

}


