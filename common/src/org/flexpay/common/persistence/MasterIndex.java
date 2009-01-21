package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Master index is an unique value among all integrated systems
 */
public class MasterIndex extends DomainObject {

	private Long index = 0L;
	private int objectType;

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("id", getId()).
				append("index", index).
				append("objectType", objectType).
				toString();
	}

	public void increment(int count) {
		index += count;
	}
}
