package org.flexpay.ab.service.impl;

import org.flexpay.ab.persistence.HistoryRec;
import org.flexpay.common.persistence.*;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractProcessor<T extends DomainObject> {

	protected final Logger log = LoggerFactory.getLogger(getClass());

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
	@NotNull
	public T createObject(@Nullable DomainObject obj, String extId, Stub<DataSourceDescription> sd, CorrectionsService cs)
			throws Exception {
		if (obj == null) {
			Stub<T> stub = cs.findCorrection(extId, type, sd);
			if (stub != null) {
				log.debug("External object already exists: {}", extId);
				T t = readObject(stub);
				if (t == null) {
					throw new IllegalStateException("Invalid correction present, no object: " + extId + ", type: " + type);
				}
				return t;
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
	@NotNull
	protected abstract T doCreateObject() throws Exception;

	/**
	 * Delete DomainObject, actually set status to disabled
	 *
	 * @param domainObject DomainObject to delete
	 */
	public void deleteObject(DomainObject domainObject) {
		DomainObjectWithStatus objectWithStatus = (DomainObjectWithStatus) domainObject;
		if (objectWithStatus == null) {
			return;
		}
		objectWithStatus.disable();
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object stub
	 * @return DomainObject instance
	 */
	@Nullable
	protected abstract T readObject(@NotNull Stub<T> stub);

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
	@NotNull
	public T findObject(@Nullable DomainObject obj, String extId, Stub<DataSourceDescription> sd, CorrectionsService cs)
			throws Exception {
		if (obj == null) {
			Stub<T> stub = cs.findCorrection(extId, type, sd);
			if (stub != null) {
				log.debug("External object already exists: {}", extId);

				T object = readObject(stub);
				if (object == null) {
					throw new IllegalStateException("Invalid correction present, no object: " + extId + ", type: " + type);
				}
				return object;
			}

			log.debug("Cannot find correction for object of type {} with id {}, creating a stub", type, extId);
			return doCreateObject();
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
	public abstract void setProperty(@NotNull DomainObject object, @NotNull HistoryRec record, Stub<DataSourceDescription> sd, CorrectionsService cs)
			throws Exception;

	/**
	 * Save DomainObject
	 *
	 * @param object	 Object to save
	 * @param externalId External object id
	 * @param sd		 DataSourceDescription
	 * @param cs		 CorrectionsService
	 * @throws Exception if failure occurs
	 */
	public void saveObject(DomainObject object, String externalId, Stub<DataSourceDescription> sd, CorrectionsService cs)
			throws Exception {

		log.debug("Saving object: {}, externalID: {}", object.getId(), externalId);

		Long id = object.getId();
		T obj = type.cast(object);
		if (id == null) {
			// cannot delete unknown object
			if (obj instanceof DomainObjectWithStatus) {
				DomainObjectWithStatus withStatus = (DomainObjectWithStatus) obj;
				if (withStatus.isNotActive()) {
					log.warn("Deleting unknown object nothing to do: {}", externalId);
					return;
				}
			}
		}

		Stub<T> stub = findPersistentObject(obj, sd, cs);
		if (stub != null) {
			log.info("Found similar object, need to add correction only");
			if (cs.existsCorrection(externalId, type, sd)) {
				log.debug("Correction already exists");
				return;
			}
			log.debug("Creating correction");
			// found similar object just create a reference to it
			obj.setId(stub.getId());
			DataCorrection correction = cs.getStub(externalId, obj, sd);
			cs.save(correction);
			return;
		}

		log.info("Performing save: {}", obj);
		doSaveObject(obj, externalId);

		if (id == null && obj.getId() != null) {
			log.info("Adding a new object correction: {}", obj.getId());
			DataCorrection correction = cs.getStub(externalId, obj, sd);
			cs.save(correction);
		}
	}

	/**
	 * Try to find persistent object by set properties
	 *
	 * @param object DomainObject
	 * @param sd	 DataSourceDescription
	 * @param cs	 CorrectionsService
	 * @return Persistent object stub if exists, or <code>null</code> otherwise
	 * @throws Exception if failure occurs
	 */
	@Nullable
	protected abstract Stub<T> findPersistentObject(T object, Stub<DataSourceDescription> sd, CorrectionsService cs)
			throws Exception;

	/**
	 * Save DomainObject
	 *
	 * @param object	 Object to save
	 * @param externalId External object identifier
	 * @throws Exception if failure occurs
	 */
	protected abstract void doSaveObject(T object, String externalId) throws Exception;
}
