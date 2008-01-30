package org.flexpay.sz.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SzFileActualityStatus {
	private Long id;
	private String description;

	public SzFileActualityStatus() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns a string representation of the object.
	 * 
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Id", id).append("Description", description).toString();
	}

}
