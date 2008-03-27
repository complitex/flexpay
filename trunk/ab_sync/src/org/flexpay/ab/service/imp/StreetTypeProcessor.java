package org.flexpay.ab.service.imp;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.ab.persistence.HistoryRecord;
import org.flexpay.ab.persistence.StreetType;

/**
 * Dummy implementation, does nothing usefull
 */
public class StreetTypeProcessor extends AbstractProcessor<StreetType> {

	public StreetTypeProcessor() {
		super(StreetType.class);
	}

	/**
	 * Create new DomainObject
	 *
	 * @return DomainObject
	 * @throws Exception if failure occurs
	 */
	protected StreetType doCreateObject() throws Exception {
		return new StreetType();
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	protected StreetType readObject(StreetType stub) {
		return stub;
	}

	/**
	 * Update DomainObject from HistoryRecord
	 *
	 * @param object DomainObject to set properties on
	 * @param record HistoryRecord
	 * @param sd	 DataSourceDescription
	 * @param cs	 CorrectionsService
	 * @throws Exception if failure occurs
	 */
	public void setProperty(DomainObject object, HistoryRecord record, DataSourceDescription sd, CorrectionsService cs)
			throws Exception {
	}

	/**
	 * Save DomainObject
	 *
	 * @param object Object to save
	 */
	protected void doSaveObject(StreetType object) {
	}
}