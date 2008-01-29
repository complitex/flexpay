package org.flexpay.ab.persistence.temp;
// Generated 15.11.2007 14:59:52 by Hibernate Tools 3.2.0.b11


import org.flexpay.ab.persistence.Person;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ApartmentRelation generated by hbm2java
 */
@Entity
@Table (name = "apartment_relation"
		, catalog = "flexpay_db"
)
public class ApartmentRelation implements java.io.Serializable {


	private int id;
	private ApartmentRelationType apartmentRelationType;
	private Person person;
	private Apartment apartment;
	private int owningPart;
	private Date begin;
	private Date end;
	private Set<Act> acts = new HashSet<Act>(0);

	public ApartmentRelation() {
	}


	public ApartmentRelation(int id, ApartmentRelationType apartmentRelationType, Person person, Apartment apartment, int owningPart, Date begin, Date end) {
		this.id = id;
		this.apartmentRelationType = apartmentRelationType;
		this.person = person;
		this.apartment = apartment;
		this.owningPart = owningPart;
		this.begin = begin;
		this.end = end;
	}

	public ApartmentRelation(int id, ApartmentRelationType apartmentRelationType, Person person, Apartment apartment, int owningPart, Date begin, Date end, Set<Act> acts) {
		this.id = id;
		this.apartmentRelationType = apartmentRelationType;
		this.person = person;
		this.apartment = apartment;
		this.owningPart = owningPart;
		this.begin = begin;
		this.end = end;
		this.acts = acts;
	}

	@Id

	@Column (name = "id", nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "Apartment_relation_type_ID", nullable = false)
	public ApartmentRelationType getApartmentRelationType() {
		return this.apartmentRelationType;
	}

	public void setApartmentRelationType(ApartmentRelationType apartmentRelationType) {
		this.apartmentRelationType = apartmentRelationType;
	}

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "Person_ID", nullable = false)
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "Apartment_ID", nullable = false)
	public Apartment getApartment() {
		return this.apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	@Column (name = "owning_part", nullable = false)
	public int getOwningPart() {
		return this.owningPart;
	}

	public void setOwningPart(int owningPart) {
		this.owningPart = owningPart;
	}

	@Column (name = "begin", nullable = false, length = 0)
	public Date getBegin() {
		return this.begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	@Column (name = "end", nullable = false, length = 0)
	public Date getEnd() {
		return this.end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	@ManyToMany (fetch = FetchType.LAZY, mappedBy = "apartmentRelations")
	public Set<Act> getActs() {
		return this.acts;
	}

	public void setActs(Set<Act> acts) {
		this.acts = acts;
	}
}
