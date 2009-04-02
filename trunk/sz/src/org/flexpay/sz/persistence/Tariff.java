package org.flexpay.sz.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.orgs.persistence.Organization;

import java.util.Date;

public class Tariff extends DomainObject {

	private Integer extCode;
	private Integer privilegeCode;
	private Integer serviceType;
	private Date beginDate;
	private Date endDate;
	private Long cost;
	private Integer unitCode;
	private Organization organization;

	/**
	 * Getter for property 'extCode'.
	 *
	 * @return Value for property 'extCode'.
	 */
	public Integer getExtCode() {
		return extCode;
	}

	/**
	 * Setter for property 'extCode'.
	 *
	 * @param extCode Value to set for property 'extCode'.
	 */
	public void setExtCode(Integer extCode) {
		this.extCode = extCode;
	}

	/**
	 * Getter for property 'privilegeCode'.
	 *
	 * @return Value for property 'privilegeCode'.
	 */
	public Integer getPrivilegeCode() {
		return privilegeCode;
	}

	/**
	 * Setter for property 'privilegeCode'.
	 *
	 * @param privilegeCode Value to set for property 'privilegeCode'.
	 */
	public void setPrivilegeCode(Integer privilegeCode) {
		this.privilegeCode = privilegeCode;
	}

	/**
	 * Getter for property 'serviceType'.
	 *
	 * @return Value for property 'serviceType'.
	 */
	public Integer getServiceType() {
		return serviceType;
	}

	/**
	 * Setter for property 'serviceType'.
	 *
	 * @param serviceType Value to set for property 'serviceType'.
	 */
	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * Getter for property 'beginDate'.
	 *
	 * @return Value for property 'beginDate'.
	 */
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

	/**
	 * Getter for property 'cost'.
	 *
	 * @return Value for property 'cost'.
	 */
	public Long getCost() {
		return cost;
	}

	/**
	 * Setter for property 'cost'.
	 *
	 * @param cost Value to set for property 'cost'.
	 */
	public void setCost(Long cost) {
		this.cost = cost;
	}

	/**
	 * Getter for property 'unitCode'.
	 *
	 * @return Value for property 'unitCode'.
	 */
	public Integer getUnitCode() {
		return unitCode;
	}

	/**
	 * Setter for property 'unitCode'.
	 *
	 * @param unitCode Value to set for property 'unitCode'.
	 */
	public void setUnitCode(Integer unitCode) {
		this.unitCode = unitCode;
	}

	/**
	 * Getter for property 'organization'.
	 *
	 * @return Value for property 'organization'.
	 */
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * Setter for property 'organization'.
	 *
	 * @param organization Value to set for property 'organization'.
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("Tariff {").
				append("id", getId()).
				append("extCode", extCode).
				append("privilegeCode", privilegeCode).
				append("serviceType", serviceType).
				append("beginDate", beginDate).
				append("endDate", endDate).
				append("cost", cost).
				append("unitCode", unitCode).
				append("}").toString();
	}

}
