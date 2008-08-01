package org.flexpay.common.service.importexport;

import java.util.List;

public interface RawDataSource<T extends RawData> {

	/**
	 * Check if source is trusted and new objects are allowed to be created from this source
	 *
	 * @return <code>true</code> if the source is trusted, or <code>false</code> otherwise
	 */
	boolean trusted();

	/**
	 * Find raw data by its id
	 *
	 * @param objId Raw data id
	 * @return raw data
	 */
	T getById(String objId);

	/**
	 * Initialize data source
	 */
	void initialize();

	/**
	 * Release all resources taken
	 */
	void close();

	/**
	 * Returns <tt>true</tt> if the iteration has more elements. (In other words, returns
	 * <tt>true</tt> if <tt>next</tt> would return an element rather than throwing an
	 * exception.)
	 *
	 * @return <tt>true</tt> if the iterator has more elements.
	 */
	boolean hasNext();

	/**
	 * Returns the next new imported element in the iteration.
	 *
	 * @param holder Operation type holder
	 * @return the next element in the iteration.
	 * @throws java.util.NoSuchElementException
	 *          iteration has no more elements.
	 */
	T next(ImportOperationTypeHolder holder);

	/**
	 * return next batch of data
	 * @return List of raw data, when the list is empty hasNext() should return <code>false</code> 
	 */
	List<T> nextPage();
}
