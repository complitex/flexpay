package org.flexpay.common.dao.paging;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * ListPage class used to implement paging
 */
public class Page<E> implements Serializable {

	private int pageSize;
	private int pageNumber;

	private transient int totalElements;
	private transient List<E> elements = Collections.emptyList();

	/**
	 * Constructs a new Page.
	 */
	public Page() {
		this.pageSize = 20;
		this.pageNumber = 0;
	}

	public Page(int pageSize, int pageNumber) {
		if (pageSize <= 0) {
			throw new IllegalArgumentException("Invalid page parameters");
		}
		this.pageSize = pageSize;
		this.pageNumber = pageNumber - 1;
	}

	/**
	 * Setter for property 'totalResults'.
	 *
	 * @param totalElements Value to set for property 'totalResults'.
	 */
	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
		int lastPageNumber = getLastPageNumber();
		if (pageNumber > lastPageNumber) {
			pageNumber = lastPageNumber;
		}
	}

	/**
	 * Setter for property 'results'.
	 *
	 * @param elements Value to set for property 'results'.
	 */
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

	/**
	 * Get page elements
	 *
	 * @return current page elements list
	 */
	public List<E> getPageElements() {
		return elements;
	}

	/**
	 * Get total number of elements
	 *
	 * @return number of elements
	 */
	public int getTotalNumberOfElements() {
		return totalElements;
	}

	/**
	 * Get current page first element number
	 *
	 * @return page first element number
	 */
	public int getThisPageFirstElementNumber() {
		return pageNumber * pageSize;
	}

	/**
	 * Get current page last element number
	 *
	 * @return page last element number
	 */
	public int getThisPageLastElementNumber() {
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

	/**
	 * Get number of elements in the page
	 *
	 * @return page size
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Get current page number
	 *
	 * @return page number
	 */
	public int getPageNumber() {
		return pageNumber + 1;
	}

	/**
	 * Setter for property 'pageSize'.
	 *
	 * @param pageSize Value to set for property 'pageSize'.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Setter for property 'pageNumber'.
	 *
	 * @param pageNumber Value to set for property 'pageNumber'.
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber - 1;
	}

	@Override
	public String toString() {
		return getThisPageFirstElementNumber() + " : " + (getThisPageFirstElementNumber() + pageSize);
	}
}
