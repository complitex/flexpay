package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.LangNameObject;
import org.flexpay.ab.persistence.temp.Town;

import java.util.Collections;
import java.util.Set;

/**
 * Region
 */
public class Region extends LangNameObject implements java.io.Serializable {

	public static final int STATUS_ACTIVE = 0;
	public static final int STATUS_DISABLED = 1;

	private Long id;
	private Country country;
	private int status;
	private Set<RegionName> names = Collections.emptySet();
	private Set<Town> towns = Collections.emptySet();

	// Region name in current locale
	private transient RegionName regionName;

	/** Constructs a new Region. */
	public Region() {
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
	 * Getter for property 'country'.
	 *
	 * @return Value for property 'country'.
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * Setter for property 'country'.
	 *
	 * @param country Value to set for property 'country'.
	 */
	public void setCountry(Country country) {
		this.country = country;
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
	 * Getter for property 'names'.
	 *
	 * @return Value for property 'names'.
	 */
	public Set<RegionName> getNames() {
		return names;
	}

	/**
	 * Setter for property 'names'.
	 *
	 * @param names Value to set for property 'names'.
	 */
	public void setNames(Set<RegionName> names) {
		this.names = names;
	}

	/**
	 * Getter for property 'towns'.
	 *
	 * @return Value for property 'towns'.
	 */
	public Set<Town> getTowns() {
		return this.towns;
	}

	/**
	 * Setter for property 'towns'.
	 *
	 * @param towns Value to set for property 'towns'.
	 */
	public void setTowns(Set<Town> towns) {
		this.towns = towns;
	}

	/**
	 * Getter for property 'regionName'.
	 *
	 * @return Value for property 'regionName'.
	 */
	public RegionName getRegionName() {
		return regionName;
	}

	/**
	 * Setter for property 'regionName'.
	 *
	 * @param regionName Value to set for property 'regionName'.
	 */
	public void setRegionName(RegionName regionName) {
		this.regionName = regionName;
	}
}
