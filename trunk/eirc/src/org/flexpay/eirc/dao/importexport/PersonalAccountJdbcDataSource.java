package org.flexpay.eirc.dao.importexport;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.eirc.dao.importexport.imp.HarkovCenterNachisleniyDataSource;
import org.flexpay.eirc.service.importexport.RawPersonalAccountData;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PersonalAccountJdbcDataSource implements RawDataSource<RawPersonalAccountData> {

	private HarkovCenterNachisleniyDataSource source;
	private Page<RawPersonalAccountData> pager;
	private Iterator<RawPersonalAccountData> dataIterator;

	private Set<String> forbiddenPersons = new HashSet<String>();

	{
		forbiddenPersons.add("ЛИЦЕВОЙ ЗАКРЫТ");
	}

	public boolean trusted() {
		return true;
	}

	public RawPersonalAccountData getById(String objId) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * Initialize data source
	 */
	public void initialize() {
		pager = new Page<RawPersonalAccountData>(1000, 1);
		List<RawPersonalAccountData> datas = source.getPersonalAccountData(pager);
		dataIterator = datas.iterator();
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
		List<RawPersonalAccountData> datas = source.getPersonalAccountData(pager);
		dataIterator = datas.iterator();
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
	public RawPersonalAccountData next(ImportOperationTypeHolder holder) {
		RawPersonalAccountData data = dataIterator.next();
		if (forbiddenPersons.contains(data.getLastName())) {
			return null;
		}

		return data;
	}

	/**
	 * Setter for property 'source'.
	 *
	 * @param source Value to set for property 'source'.
	 */
	public void setSource(HarkovCenterNachisleniyDataSource source) {
		this.source = source;
	}

	public Set<String> getForbiddenPersons() {
		return forbiddenPersons;
	}
}
