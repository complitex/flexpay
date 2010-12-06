package org.flexpay.common.persistence.filter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Simple class for results of search in filters
 *
 * value - field for hidden-value (for example - found object id)
 * name - field for view-value (for example - found object name)
 *
 */
public class FilterObject implements Serializable {

	private String value;
	private String name;

	public FilterObject() {
	}

	public FilterObject(String value, String name) {
		this.value = value;
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("value", value).
				append("name", name).toString();
	}

}
