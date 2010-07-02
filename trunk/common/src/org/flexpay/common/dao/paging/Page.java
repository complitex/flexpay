package org.flexpay.common.dao.paging;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * ListPage class used to implement paging
 */
public class Page<E> implements Serializable {

	private int pageSize = 20;
	private int pageNumber = 0;

	private transient int totalElements = 0;
	private transient List<E> elements = Collections.emptyList();

	public Page() {
	}

	public Page(Integer pageSize) {
		if (pageSize != null && pageSize > 0) {
			this.pageSize = pageSize;
		}
	}

	public Page(int pageSize, int pageNumber) {
		if (pageSize <= 0) {
			throw new IllegalArgumentException("Invalid page parameters");
		}
		this.pageSize = pageSize;
		this.pageNumber = pageNumber - 1;
	}

	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
		int lastPageNumber = getLastPageNumber();
		if (pageNumber > lastPageNumber) {
			pageNumber = lastPageNumber;
		}
	}

	public void setElements(List<E> elements) {
		this.elements = elements;
	}

	/**
	 * Check if current page is the first one
	 *
	 * @return <code>true</code> if current page is the first, or <code>false</code>
	 *         otherwise
	 */
	public boolean isFirstPage() {
		return pageNumber == 0;
	}

	/**
	 * Check if current page is the last one
	 *
	 * @return <code>true</code> if current page is the last, or <code>false</code>
	 *         otherwise
	 */
	public boolean isLastPage() {
		return pageNumber == getLastPageNumber() - 1;
	}

	/**
	 * Check if pager has more pages
	 *
	 * @return <code>true</code> if there are more pages available, or <code>false</code>
	 *         otherwise
	 */
	public boolean hasNextPage() {
		return pageNumber < getLastPageNumber() - 1;
	}

	/**
	 * Check if pager has previous pages
	 *
	 * @return <code>true</code> if there are previous pages available, or <code>false</code>
	 *         otherwise
	 */
	public boolean hasPreviousPage() {
		return pageNumber > 0 && getLastPageNumber() > 0;
	}

	/**
	 * Get last page number
	 *
	 * @return page number
	 */
	public int getLastPageNumber() {
		int div = totalElements / pageSize;
		return div * pageSize < totalElements ? div + 1 : div;
	}

	public List<E> getPageElements() {
		return elements;
	}

	public int getTotalNumberOfElements() {
		return totalElements;
	}

	/**
	 * Get current page first element number
	 *
	 * @return page first element number
	 */
	public int getThisPageFirstElementNumber() {
		if (!hasNextPage()) {
			pageNumber = getLastPageNumber() == 0 ? 0 : getLastPageNumber() - 1;
		}
		return pageNumber * pageSize;
	}

	/**
	 * Get current page last element number
	 *
	 * @return page last element number
	 */
	public int getThisPageLastElementNumber() {
		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (elements == Collections.EMPTY_LIST) {
			return getThisPageFirstElementNumber() + pageSize;
		}
		return getThisPageFirstElementNumber() + elements.size();
	}

	/**
	 * Get next page number
	 *
	 * @return next page number
	 */
	public int getNextPageNumber() {
		return hasNextPage() ? getPageNumber() + 1 : getLastPageNumber();
	}

	/**
	 * Get previous page number
	 *
	 * @return previous page number
	 */
	public int getPreviousPageNumber() {
		return hasPreviousPage() ? getPageNumber() - 1 : 1;
	}

	public void nextPage() {
		setPageNumber(getPageNumber() + 1);
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getPageNumber() {
		return pageNumber + 1;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageNumber(int pageNumber) {
		if (pageNumber <= 0) {
			pageNumber = 1;
		}
		this.pageNumber = pageNumber - 1;
	}

	public void moveFirstPage() {
		setPageNumber(1);
	}

	public void moveLastPage() {
		setPageNumber(getLastPageNumber());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("pageSize", pageSize).
				append("pageNumber", pageNumber).
				append("totalElements", totalElements).
				toString();
	}

}
