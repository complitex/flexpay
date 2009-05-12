package org.flexpay.bti.persistence;
// Generated 15.11.2007 14:59:52 by Hibernate Tools 3.2.0.b11


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * ApartmentRelationType generated by hbm2java
 */
@Entity
@Table (name = "apartment_relation_type"
		, catalog = "flexpay_db"
)
public class ApartmentRelationType implements java.io.Serializable {


	private int id;
	private String name;
	private Set<ApartmentRelation> apartmentRelations = new HashSet<ApartmentRelation>(0);

	public ApartmentRelationType() {
	}


	public ApartmentRelationType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public ApartmentRelationType(int id, String name, Set<ApartmentRelation> apartmentRelations) {
		this.id = id;
		this.name = name;
		this.apartmentRelations = apartmentRelations;
	}

	@Id

	@Column (name = "id", nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column (name = "Name", nullable = false, length = 120)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany (fetch = FetchType.LAZY, mappedBy = "apartmentRelationType")
	public Set<ApartmentRelation> getApartmentRelations() {
		return this.apartmentRelations;
	}

	public void setApartmentRelations(Set<ApartmentRelation> apartmentRelations) {
		this.apartmentRelations = apartmentRelations;
	}

}

