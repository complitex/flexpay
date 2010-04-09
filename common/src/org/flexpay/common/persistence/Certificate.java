package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

public class Certificate extends DomainObject {

	private String alias;
	private String description;
	private Date beginDate;
	private Date endDate;

	public Certificate() {
	}

	public Certificate(String alias, String description, Date beginDate, Date endDate) {
		this.alias = alias;
		this.description = description;
		this.beginDate = beginDate;
		this.endDate = endDate;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("version", version).
				append("alias", alias).
				append("description", description).
				toString();
	}
}
