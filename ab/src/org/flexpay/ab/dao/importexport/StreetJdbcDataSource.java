package org.flexpay.ab.dao.importexport;

import org.flexpay.ab.dao.importexport.imp.HarkovCenterNachisleniyDataSource;
import org.flexpay.ab.service.importexport.RawStreetData;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.common.service.importexport.RawDataSource;

import java.util.Iterator;
import java.util.List;

public class StreetJdbcDataSource implements RawDataSource<RawStreetData> {

	private HarkovCenterNachisleniyDataSource source;
	private Page<RawStreetData> pager;
	private Iterator<RawStreetData> dataIterator;

	/**
	 * Initialize data source
	 */
	public void initialize() {
		pager = new Page<RawStreetData>();
		List<RawStreetData> districtDatas = source.getStreetsData(pager);
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
		List<RawStreetData> districtDatas = source.getStreetsData(pager);
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
	public RawStreetData next(ImportOperationTypeHolder holder) {
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