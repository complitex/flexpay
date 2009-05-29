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

	public Integer getExtCode() {
		return extCode;
	}

	public void setExtCode(Integer extCode) {
		this.extCode = extCode;
	}

	public Integer getPrivilegeCode() {
		return privilegeCode;
	}

	public void setPrivilegeCode(Integer privilegeCode) {
		this.privilegeCode = privilegeCode;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getCost() {
		return cost;
	}

	public void setCost(Long cost) {
		this.cost = cost;
	}

	public Integer getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(Integer unitCode) {
		this.unitCode = unitCode;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("extCode", extCode).
				append("privilegeCode", privilegeCode).
				append("serviceType", serviceType).
				append("beginDate", beginDate).
				append("endDate", endDate).
				append("cost", cost).
				append("unitCode", unitCode).
				toString();
	}

}
