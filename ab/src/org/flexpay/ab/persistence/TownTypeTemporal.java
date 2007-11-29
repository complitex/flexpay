package org.flexpay.ab.persistence;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table (name = "town_type_temporals_tbl")
public class TownTypeTemporal implements Serializable {
	private Long id;
	private TownType townType;
	private Date beginDate;
	private Date endDate;

	/**
	 * Constructs a new TownTypeTemporal.
	 */
	public TownTypeTemporal() {
	}

	/**
	 * Getter for property 'id'.
	 *
	 * @return Value for property 'id'.
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
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
	 * Getter for property 'townType'.
	 *
	 * @return Value for property 'townType'.
	 */
	@ManyToOne
	@JoinColumn (name = "town_type_id", nullable = false)
	public TownType getTownType() {
		return townType;
	}

	/**
	 * Setter for property 'townType'.
	 *
	 * @param townType Value to set for property 'townType'.
	 */
	public void setTownType(TownType townType) {
		this.townType = townType;
	}

	/**
	 * Getter for property 'beginDate'.
	 *
	 * @return Value for property 'beginDate'.
	 */
	@Temporal (value = TemporalType.TIMESTAMP)
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * Setter for property 'beginDate'.
	 *
	 * @param beginDate Value to set for property 'beginDate'.
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * Getter for property 'endDate'.
	 *
	 * @return Value for property 'endDate'.
	 */
	@Temporal (value = TemporalType.TIMESTAMP)
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Setter for property 'endDate'.
	 *
	 * @param endDate Value to set for property 'endDate'.
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
