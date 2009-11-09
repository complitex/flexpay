package org.flexpay.ab.dao.importexport;

import org.flexpay.ab.dao.importexport.impl.HarkovCenterNachisleniyDataSource;
import org.flexpay.ab.service.importexport.RawStreetTypeData;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.common.service.importexport.RawDataSource;
import org.springframework.beans.factory.annotation.Required;

import java.util.Iterator;
import java.util.List;

public class StreetTypeJdbcDataSource implements RawDataSource<RawStreetTypeData> {

	private HarkovCenterNachisleniyDataSource source;
	private Page<RawStreetTypeData> pager;
	private Iterator<RawStreetTypeData> dataIterator;

	@Override
	public boolean trusted() {
		return true;
	}

	@Override
	public RawStreetTypeData getById(String objId) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * Initialize data source
	 */
	@Override
	public void initialize() {
		pager = new Page<RawStreetTypeData>();
		List<RawStreetTypeData> districtDatas = source.getStreetTypesData(pager);
		dataIterator = districtDatas.iterator();
	}

	/**
	 * Release all resources taken
	 */
	@Override
	public void close() {
	}

	/**
	 * Returns <tt>true</tt> if the iteration has more elements. (In other words, returns <tt>true</tt> if <tt>next</tt> would return
	 * an element rather than throwing an exception.)
	 *
	 * @return <tt>true</tt> if the iterator has more elements.
	 */
	@Override
	public boolean hasNext() {
		if (dataIterator.hasNext()) {
			return true;
		}

		// get next page
		int nextPage = pager.getPageNumber() + 1;
		pager.setPageNumber(nextPage);
		List<RawStreetTypeData> districtDatas = source.getStreetTypesData(pager);
		dataIterator = districtDatas.iterator();
		return dataIterator.hasNext();
	}

	/**
	 * Returns the next new imported element in the iteration.
	 *
	 * @param holder Operation type holder
	 * @return the next element in the iteration.
	 * @throws java.util.NoSuchElementException
	 *          iteration has no more elements.
	 */
	@Override
	public RawStreetTypeData next(ImportOperationTypeHolder holder) {
		return dataIterator.next();
	}

	/**
	 * return next batch of data
	 *
	 * @return List of raw data, when the list is empty hasNext() should return <code>false</code>
	 */
	@Override
	public List<RawStreetTypeData> nextPage() {
		throw new RuntimeException("Not implemented");
	}

	/**
	 * Setter for property 'source'.
	 *
	 * @param source Value to set for property 'source'.
	 */
	@Required
	public void setSource(HarkovCenterNachisleniyDataSource source) {
		this.source = source;
	}

}
