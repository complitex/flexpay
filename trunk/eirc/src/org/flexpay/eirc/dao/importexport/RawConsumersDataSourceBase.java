package org.flexpay.eirc.dao.importexport;

import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public abstract class RawConsumersDataSourceBase implements RawDataSource<RawConsumerData> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	protected Iterator<RegistryRecord> dataIterator;

	/**
	 * Check if source is trusted and new objects are allowed to be created from this source
	 *
	 * @return <code>true</code> if the source is trusted, or <code>false</code> otherwise
	 */
	public boolean trusted() {
		return false;
	}

	protected RawConsumerData convert(RegistryRecord record) {
		return RawConsumersDataUtil.convert(record);
	}

	/**
	 * Returns the next new imported element in the iteration.
	 *
	 * @param holder Operation type holder
	 * @return the next element in the iteration.
	 * @throws java.util.NoSuchElementException
	 *          iteration has no more elements.
	 */
	public RawConsumerData next(ImportOperationTypeHolder holder) {
		return convert(dataIterator.next());
	}
}
