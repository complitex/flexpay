package org.flexpay.common.service.importexport.imp;

import org.flexpay.common.dao.CorrectionsDao;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class CorrectionsServiceImpl implements CorrectionsService {

	private ClassToTypeRegistry typeRegistry;
	private CorrectionsDao correctionsDao;

	/**
	 * Create data correction
	 *
	 * @param correction DataCorrection
	 */
	public void save(DataCorrection correction) {
		correctionsDao.save(correction);
	}

	/**
	 * Find domain object by its external data source id
	 *
	 * @param externalId		External id
	 * @param cls			   Object class to find
	 * @param sourceDescription External data source description
	 * @return DomainObject
	 */
	public DomainObject findCorrection(String externalId, Class<?> cls, DataSourceDescription sourceDescription) {
		int type = typeRegistry.getType(cls);
		return correctionsDao.findCorrection(externalId, type, cls, sourceDescription);
	}

	/**
	 * Check if correction exists
	 *
	 * @param externalId		External id
	 * @param cls			   Object class to find
	 * @param sourceDescription External data source description
	 * @return DomainObject
	 */
	public boolean existsCorrection(String externalId, Class<?> cls, DataSourceDescription sourceDescription) {
		int type = typeRegistry.getType(cls);
		return correctionsDao.existsCorrection(externalId, type, cls, sourceDescription);
	}

	/**
	 * Create stub for new data correction
	 *
	 * @param externalId External object id
	 * @param obj DomainObject
	 * @param sourceDescription Data source description
	 * @return stub for a new DataCorrection
	 */
	public DataCorrection getStub(String externalId, DomainObject obj, DataSourceDescription sourceDescription) {
		DataCorrection correction = new DataCorrection();
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