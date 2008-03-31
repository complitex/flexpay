package org.flexpay.ab.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.ab.persistence.HistoryRecord;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.service.importexport.CorrectionsService;

public abstract class AbstractProcessor<T extends DomainObject> {

	// Logger
	protected final Logger log = Logger.getLogger(getClass());

	private Class<T> type;

	protected AbstractProcessor(Class<T> type) {
		this.type = type;
	}

	/**
	 * Create new DomainObject from HistoryRecord
	 *
	 * @param obj   Domain object
	 * @param extId External object id
	 * @param sd	DataSourceDescription
	 * @param cs	CorrectionsService
	 * @return DomainObject
	 * @throws Exception if failure occurs
	 */
	public T createObject(DomainObject obj, String extId, DataSourceDescription sd, CorrectionsService cs)
			throws Exception {
		if (obj == null) {
			T stub = cs.findCorrection(extId, type, sd);
			if (stub != null) {
				log.info("External object already exists: " + extId);
				return readObject(stub);
			}

			return doCreateObject();
		}

		if (!type.isInstance(obj)) {
			throw new IllegalArgumentException("Object of type " + obj.getClass() +
					" cannot be handled by processor " + getClass());
		}

		return type.cast(obj);
	}

	/**
	 * Create new DomainObject
	 *
	 * @return DomainObject
	 * @throws Exception if failure occurs
	 */
	protected abstract T doCreateObject() throws Exception;

	/**
	 * Delete DomainObject, actually set status to disabled
	 *
	 * @param domainObject DomainObject to delete
	 */
	public void deleteObject(DomainObject domainObject) {
		DomainObjectWithStatus objectWithStatus = (DomainObjectWithStatus) domainObject;
		objectWithStatus.setStatus(DomainObjectWithStatus.STATUS_DISABLED);
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	protected abstract T readObject(T stub);

	/**
	 * Create new DomainObject from HistoryRecord
	 *
	 * @param obj   Domain object
	 * @param extId External object id
	 * @param sd	DataSourceDescription
	 * @param cs	CorrectionsService
	 * @return DomainObject
	 * @throws Exception if failure occurs
	 */
	public T findObject(DomainObject obj, String extId, DataSourceDescription sd, CorrectionsService cs)
			throws Exception {
		if (obj == null) {
			T stub = cs.findCorrection(extId, type, sd);
			if (stub != null) {
				log.info("External object already exists: " + extId);
				return readObject(stub);
			}

			throw new IllegalArgumentException("Cannot find correction for object of type " + type + " with id " + extId);
		}

		if (!type.isInstance(obj)) {
			throw new IllegalArgumentException("Object of type " + obj.getClass() + " cannot be handled by processor " + getClass());
		}

		return type.cast(obj);
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
	public abstract void setProperty(DomainObject object, HistoryRecord record, DataSourceDescription sd, CorrectionsService cs)
			throws Exception;

	/**
	 * Save DomainObject
	 *
	 * @param object	 Object to save
	 * @param externalId External object id
	 * @param sd		 DataSourceDescription
	 * @param cs		 CorrectionsService
	 */
	public void saveObject(DomainObject object, String externalId, DataSourceDescription sd, CorrectionsService cs) {
		Long id = object.getId();
		T obj = type.cast(object);
		doSaveObject(obj);

		// created a new object, need to add a correction
		if (id == null) {
			DataCorrection correction = cs.getStub(externalId, obj, sd);
			cs.save(correction);
		}
	}

	/**
	 * Save DomainObject
	 *
	 * @param object Object to save
	 */
	protected abstract void doSaveObject(T object);
}
