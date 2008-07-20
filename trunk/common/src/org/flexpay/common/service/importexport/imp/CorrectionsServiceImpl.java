package org.flexpay.common.service.importexport.imp;

import org.apache.log4j.Logger;
import org.flexpay.common.dao.CorrectionsDao;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.jetbrains.annotations.NonNls;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true)
public class CorrectionsServiceImpl implements CorrectionsService {

	@NonNls
	private Logger log = Logger.getLogger(getClass());

	private ClassToTypeRegistry typeRegistry;
	private CorrectionsDao correctionsDao;

	/**
	 * Create data correction
	 *
	 * @param correction DataCorrection
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public void save(DataCorrection correction) {
		DataCorrection corr = correctionsDao.findCorrection(
				correction.getExternalId(), correction.getObjectType(), correction.getDataSourceDescription());
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
	 * Find domain object by its external data source id
	 *
	 * @param externalId External id
	 * @param cls		Object class to find
	 * @param sd		 External data source description
	 * @return DomainObject
	 */
	public <T extends DomainObject> Stub<T> findCorrection(String externalId, Class<T> cls, DataSourceDescription sd) {
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
	public boolean existsCorrection(String externalId, Class<? extends DomainObject> cls, DataSourceDescription sd) {
		int type = typeRegistry.getType(cls);
		return correctionsDao.existsCorrection(externalId, type, sd);
	}

	/**
	 * Create stub for new data correction
	 *
	 * @param externalId		External object id
	 * @param obj			   DomainObject
	 * @param sourceDescription Data source description
	 * @return stub for a new DataCorrection
	 */
	public DataCorrection getStub(String externalId, DomainObject obj, DataSourceDescription sourceDescription) {

		int type = typeRegistry.getType(obj.getClass());
		DataCorrection correction = correctionsDao.findCorrection(externalId, type, sourceDescription);
		if (correction == null) {
			correction = new DataCorrection();
		}

		correction.setExternalId(externalId);
		correction.setInternalObjectId(obj.getId());
		correction.setDataSourceDescription(sourceDescription);
		correction.setObjectType(typeRegistry.getType(obj.getClass()));

		return correction;
	}

	/**
	 * Setter for property 'typeRegistry'.
	 *
	 * @param typeRegistry Value to set for property 'typeRegistry'.
	 */
	public void setTypeRegistry(ClassToTypeRegistry typeRegistry) {
		this.typeRegistry = typeRegistry;
	}

	/**
	 * Setter for property 'correctionsDao'.
	 *
	 * @param correctionsDao Value to set for property 'correctionsDao'.
	 */
	public void setCorrectionsDao(CorrectionsDao correctionsDao) {
		this.correctionsDao = correctionsDao;
	}
}