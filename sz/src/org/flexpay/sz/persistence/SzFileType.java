package org.flexpay.sz.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SzFileType {

	private Long id;
	private String fileMask;
	private String description;

	public SzFileType() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Returns a string representation of the object.
	 * 
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Id", id).append("File mask", fileMask).append(
						"Description", description).toString();
	}

	public String getFileMask() {
		return fileMask;
	}

	public void setFileMask(String fileMask) {
		this.fileMask = fileMask;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
