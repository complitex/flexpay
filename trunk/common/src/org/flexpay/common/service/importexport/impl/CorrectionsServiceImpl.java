package org.flexpay.common.service.importexport.impl;

import org.flexpay.common.dao.CorrectionsDao;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true)
public class CorrectionsServiceImpl implements CorrectionsService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ClassToTypeRegistry typeRegistry;
	private CorrectionsDao correctionsDao;

	/**
	 * Create data correction
	 *
	 * @param correction DataCorrection
	 */
	@Transactional (readOnly = false)
	public void save(DataCorrection correction) {
		DataCorrection corr = correctionsDao.findCorrection(
				correction.getExternalId(), correction.getObjectType(), correction.getDataSourceDescriptionStub());
		if (corr != null) {
			if (corr.getInternalObjectId().equals(correction.getInternalObjectId())) {
				log.debug("Existing correction references to the same object");
				return;
			}

			corr.setInternalObjectId(correction.getInternalObjectId());
			correction = corr;
		}
		correctionsDao.save(correction);
	}

	/**
	 * Delete correction
	 *
	 * @param correction Data correction to delete
	 */
	@Transactional (readOnly = false)
	public void delete(@NotNull DataCorrection correction) {
		if (correction.isNotNew()) {
			correctionsDao.delete(correction);
		}
	}

	/**
	 * Find domain object by its external data source id
	 *
	 * @param externalId External id
	 * @param cls		Object class to find
	 * @param sd		 External data source description
	 * @return DomainObject
	 */
	@Nullable
	public <T extends DomainObject> Stub<T> findCorrection(String externalId, Class<T> cls, Stub<DataSourceDescription> sd) {
		int type = typeRegistry.getType(cls);
		return correctionsDao.findCorrection(externalId, type, cls, sd);
	}

	/**
	 * Check if correction exists
	 *
	 * @param externalId External id
	 * @param cls		Object class to find
	 * @param sd		 External data source description
	 * @return DomainObject
	 */
	public boolean existsCorrection(String externalId, Class<? extends DomainObject> cls, Stub<DataSourceDescription> sd) {
		int type = typeRegistry.getType(cls);
		return correctionsDao.existsCorrection(externalId, type, sd);
	}

	/**
	 * Create stub for new data correction or get existing one
	 *
	 * @param externalId		External object id
	 * @param obj			   DomainObject
	 * @param sourceDescription Data source description
	 * @return stub for a new DataCorrection
	 */
	@NotNull
	public DataCorrection getStub(String externalId, DomainObject obj, Stub<DataSourceDescription> sourceDescription) {

		int type = typeRegistry.getType(obj.getClass());
		DataCorrection correction = correctionsDao.findCorrection(externalId, type, sourceDescription);
		if (correction == null) {
			correction = new DataCorrection();
		}

		correction.setExternalId(externalId);
		correction.setInternalObjectId(obj.getId());
		correction.setDataSourceDescription(new DataSourceDescription(sourceDescription));
		correction.setObjectType(typeRegistry.getType(obj.getClass()));

		return correction;
	}

	public String getExternalId(Long internalId, int type, Stub<DataSourceDescription> dataSourceDescriptionStub) {
		return correctionsDao.getExternalId(internalId, type, dataSourceDescriptionStub.getId());
	}

	/**
	 * Find external identifier of internal object
	 *
	 * @param obj			   Object to get external identifier of
	 * @param sd DataSourceDescription to get
	 * @return External id that if found, or <code>null</code> otherwise
	 */
	@Nullable
	public <T extends DomainObject> String getExternalId(@NotNull T obj, Stub<DataSourceDescription> sd) {
		return correctionsDao.getExternalId(obj.getId(), typeRegistry.getType(obj.getClass()), sd.getId());
	}

	@Required
	public void setTypeRegistry(ClassToTypeRegistry typeRegistry) {
		this.typeRegistry = typeRegistry;
	}

	@Required
	public void setCorrectionsDao(CorrectionsDao correctionsDao) {
		this.correctionsDao = correctionsDao;
	}

}
