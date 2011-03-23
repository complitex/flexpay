package org.flexpay.eirc.dao.importexport;

import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.service.importexport.RawConsumerData;

import java.util.Collection;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

public class InMemoryRawConsumersDataSource extends RawConsumersDataSourceBase {

	private Collection<RegistryRecord> records;

	public InMemoryRawConsumersDataSource(Collection<RegistryRecord> records) {
		this.records = records;
		dataIterator = records.iterator();

		log.debug("Created inmemory data source");
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
		for (RegistryRecord record : records) {
			if (stub(record).getId().equals(id)) {
				return convert(record);
			}
		}
		return null;
	}

	/**
	 * Initialize data source
	 */
	public void initialize() {

		log.debug("Inited inmemory data source");
	}

	/**
	 * Release all resources taken
	 */
	public void close() {
	}

	/**
	 * Returns <tt>true</tt> if the iteration has more elements. (In other words, returns <tt>true</tt> if <tt>next</tt> would return
	 * an element rather than throwing an exception.)
	 *
	 * @return <tt>true</tt> if the iterator has more elements.
	 */
	public boolean hasNext() {
		return dataIterator.hasNext();
	}

	public List<RawConsumerData> nextPage() {

		List<RawConsumerData> result = CollectionUtils.list();
		while (hasNext()) {
			result.add(next(new ImportOperationTypeHolder()));
		}
		return result;
	}
}
