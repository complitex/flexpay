package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.LangNameObject;

import javax.persistence.*;

/**
 * Region
 */
@Entity
@Table (name = "region", catalog = "flexpay_db")
public class Region extends LangNameObject implements java.io.Serializable {

	private int id;
	private Country country;
	private RegionStatus regionStatus;
//	private Set<Town> towns = new HashSet<Town>(0);

	public Region() {
	}

	public Region(Country country, RegionStatus regionStatus) {
		this.country = country;
		this.regionStatus = regionStatus;
	}

	@Id
	@Column (name = "ID", nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "Country_ID", nullable = false)
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Column (name = "Region_Status", nullable = false)
	public RegionStatus getRegionStatus() {
		return regionStatus;
	}

	public void setRegionStatus(RegionStatus regionStatus) {
		this.regionStatus = regionStatus;
	}

//	@OneToMany (fetch = FetchType.LAZY, mappedBy = "region")
//	public Set<Town> getTowns() {
//		return this.towns;
//	}
//
//	public void setTowns(Set<Town> towns) {
//		this.towns = towns;
//	}
}
