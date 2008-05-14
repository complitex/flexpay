package org.flexpay.eirc.dao.importexport;

import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.service.importexport.RawConsumerData;

import java.util.Collection;
import java.util.Iterator;

public class InMemoryRawConsumersDataSource extends RawConsumersDataSourceBase {

	private Collection<SpRegistryRecord> records;
	private Iterator<SpRegistryRecord> dataIterator;

	public InMemoryRawConsumersDataSource(Collection<SpRegistryRecord> records) {
		this.records = records;
		dataIterator = records.iterator();
	}

	/**
	 * Check if source is trusted and new objects are allowed to be created from this source
	 *
	 * @return <code>true</code> if the source is trusted, or <code>false</code> otherwise
	 */
	public boolean trusted() {
		return false;
	}

	/**
	 * Find raw data by its id
	 *
	 * @param objId Raw data id
	 * @return raw data
	 */
	public RawConsumerData getById(String objId) {
		Long id = Long.valueOf(objId);
		for (SpRegistryRecord record : records) {
			if (record.getId().equals(id)) {
				return convert(record);
			}
		}
		return null;
	}

	/**
	 * Initialize data source
	 */
	public void initialize() {
	}

	/**
	 * Release all resources taken
	 */
	public void close() {
	}

	/**
	 * Returns <tt>true</tt> if the iteration has more elements. (In other words, returns
	 * <tt>true</tt> if <tt>next</tt> would return an element rather than throwing an
	 * exception.)
	 *
	 * @return <tt>true</tt> if the iterator has more elements.
	 */
	public boolean hasNext() {
		return dataIterator.hasNext();
	}
}
