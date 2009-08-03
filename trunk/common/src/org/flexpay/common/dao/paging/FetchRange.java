package org.flexpay.common.dao.paging;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * useful when paging for records by ids interval
 */
public class FetchRange implements Serializable {

	private Long minId;
	private Long maxId;
	private int count;

	private Long lowerBound;
	private Long upperBound;

	private int pageSize = 20;

	public FetchRange() {
	}

	public FetchRange(int pageSize) {
		this.pageSize = pageSize;
	}

	public Long getMinId() {
		return minId;
	}

	public void setMinId(Long minId) {
		this.minId = minId;
	}

	public Long getMaxId() {
		return maxId;
	}

	public void setMaxId(Long maxId) {
		this.maxId = maxId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void moveBounds(int pageSize) {

		if (lowerBound != null && upperBound != null) {
			lowerBound = upperBound + 1;
			upperBound = lowerBound + pageSize;
		}
	}

	public void nextPage() {
		moveBounds(pageSize);
	}

	/**
	 * Check if there is more elements to fetch
	 *
	 * @return <code>true</code> if next fetch may be succesfull, or <code>false</code> otherwise
	 */
	public boolean hasMore() {
		return !(lowerBound == null || maxId == null) && lowerBound.compareTo(maxId) < 0;
	}

	public boolean wasInitialized() {
		return maxId != null;
	}

	@Override
	public String toString() {
		long percent = (maxId == null || minId == null || lowerBound == null || upperBound == null) ?
					   0 :
					   (maxId.equals(minId)) ?
					   100 :
					   (long)(100.0 * (lowerBound - minId) / (maxId - minId));
		return new ToStringBuilder(this).
				append("minId", minId).
				append("maxId", maxId).
				append("count", count).
				append("lowerBound", lowerBound).
				append("upperBound", upperBound).
				append("pageSize", pageSize).
				append(" " + percent + "% ").
				toString();
	}

}
