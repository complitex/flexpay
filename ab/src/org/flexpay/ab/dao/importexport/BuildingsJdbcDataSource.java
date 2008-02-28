package org.flexpay.ab.dao.importexport;

import org.flexpay.ab.dao.importexport.imp.HarkovCenterNachisleniyDataSource;
import org.flexpay.ab.service.importexport.RawBuildingsData;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.common.service.importexport.RawDataSource;

import java.util.Iterator;
import java.util.List;

public class BuildingsJdbcDataSource implements RawDataSource<RawBuildingsData> {

	private HarkovCenterNachisleniyDataSource source;
	private Page<RawBuildingsData> pager;
	private Iterator<RawBuildingsData> dataIterator;

	public boolean trusted() {
		return true;
	}

	public RawBuildingsData getById(String objId) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * Initialize data source
	 */
	public void initialize() {
		pager = new Page<RawBuildingsData>(300, 1);
		List<RawBuildingsData> districtDatas = source.getBuildingsData(pager);
		dataIterator = districtDatas.iterator();
	}

	/**
	 * Returns <tt>true</tt> if the iteration has more elements. (In other words, returns
	 * <tt>true</tt> if <tt>next</tt> would return an element rather than throwing an
	 * exception.)
	 *
	 * @return <tt>true</tt> if the iterator has more elements.
	 */
	public boolean hasNext() {
		if (dataIterator.hasNext()) {
			return true;
		}

		// get next page
		int nextPage = pager.getPageNumber() + 1;
		pager.setPageNumber(nextPage);
		List<RawBuildingsData> districtDatas = source.getBuildingsData(pager);
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
	public RawBuildingsData next(ImportOperationTypeHolder holder) {
		return dataIterator.next();
	}

	/**
	 * Setter for property 'source'.
	 *
	 * @param source Value to set for property 'source'.
	 */
	public void setSource(HarkovCenterNachisleniyDataSource source) {
		this.source = source;
	}
}
