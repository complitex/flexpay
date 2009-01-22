package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Master index bounds represents available system indexes for specified object type, when lowerBound became greater
 * than upperBound new request for a batch of indexes required
 */
public class MasterIndexBounds extends DomainObject {

	private Long lowerBound = 0L;
	private Long upperBound = 0L;
	private int objectType;

	/**
	 * Constructs a new DomainObject.
	 */
	public MasterIndexBounds() {
	}

	public MasterIndexBounds(@NotNull Long id) {
		super(id);
	}

	public MasterIndexBounds(int type) {
		this.objectType = type;
	}

	public Long getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(Long lowerBound) {
		this.lowerBound = lowerBound;
	}

	public Long getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(Long upperBound) {
		this.upperBound = upperBound;
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
				append("lowerBound", lowerBound).
				append("upperBound", upperBound).
				append("objectType", objectType).
				toString();
	}

	public int getSize() {
		return (int)(upperBound - lowerBound);
	}

	public void increment(int number) {
		lowerBound += number;
	}
}