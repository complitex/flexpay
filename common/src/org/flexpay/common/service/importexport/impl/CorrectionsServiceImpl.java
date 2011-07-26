package org.flexpay.common.service.importexport.impl;

import org.flexpay.common.dao.CorrectionDao;
import org.flexpay.common.dao.CorrectionDaoExt;
import org.flexpay.common.dao.paging.Page;
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
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class CorrectionsServiceImpl implements CorrectionsService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ClassToTypeRegistry typeRegistry;
	private CorrectionDaoExt correctionDaoExt;
    private CorrectionDao correctionDao;

	/**
	 * Create data correction
	 *
	 * @param correction DataCorrection
	 */
	@Transactional (readOnly = false, propagation = Propagation.REQUIRED)
    @Override
	public void save(DataCorrection correction) {
		DataCorrection corr = correctionDaoExt.findCorrection(
				correction.getExternalId(), correction.getObjectType(), correction.getDataSourceDescriptionStub());
		if (corr != null) {
			if (corr.getInternalObjectId().equals(correction.getInternalObjectId())) {
				log.debug("Existing correction references to the same object");
				return;
			}

			corr.setInternalObjectId(correction.getInternalObjectId());
			correction = corr;
		}
		correctionDaoExt.save(correction);
	}

    @Override
    public List<DataCorrection> find(Long internalId, int type, @NotNull Page<DataCorrection> pager) {
        return correctionDao.find(internalId, type, pager);
    }

    @Override
    public DataCorrection read(Stub<DataCorrection> stub) {
        return correctionDao.readFull(stub.getId());
    }

    /**
	 * Delete correction
	 *
	 * @param correction Data correction to delete
	 */
	@Transactional (readOnly = false, propagation = Propagation.REQUIRED)
    @Override
	public void delete(@NotNull DataCorrection correction) {
		if (correction.isNotNew()) {
			correctionDaoExt.delete(correction);
		}
	}

    @Transactional (readOnly = false)
    @Override
    public void delete(@NotNull Set<Long> objectIds, int type) {
        for (Long id : objectIds) {
            DataCorrection correction = correctionDao.read(id);
            if (correction != null) {
                correctionDao.delete(correction);
            }
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
    @Override
	public <T extends DomainObject> Stub<T> findCorrection(String externalId, Class<T> cls, Stub<DataSourceDescription> sd) {
		int type = typeRegistry.getType(cls);
		return correctionDaoExt.findCorrection(externalId, type, cls, sd);
	}

	/**
	 * Check if correction exists
	 *
	 * @param externalId External id
	 * @param cls		Object class to find
	 * @param sd		 External data source description
	 * @return DomainObject
	 */
    @Override
	public boolean existsCorrection(String externalId, Class<? extends DomainObject> cls, Stub<DataSourceDescription> sd) {
		int type = typeRegistry.getType(cls);
		return correctionDaoExt.existsCorrection(externalId, type, sd);
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
    @Override
	public DataCorrection getStub(String externalId, DomainObject obj, Stub<DataSourceDescription> sourceDescription) {

		int type = typeRegistry.getType(obj.getClass());
		DataCorrection correction = correctionDaoExt.findCorrection(externalId, type, sourceDescription);
		if (correction == null) {
			correction = new DataCorrection();
		}

		correction.setExternalId(externalId);
		correction.setInternalObjectId(obj.getId());
		correction.setDataSourceDescription(new DataSourceDescription(sourceDescription));
		correction.setObjectType(typeRegistry.getType(obj.getClass()));

		return correction;
	}

    @Override
	public String getExternalId(Long internalId, int type, Stub<DataSourceDescription> dataSourceDescriptionStub) {
		return correctionDaoExt.getExternalId(internalId, type, dataSourceDescriptionStub.getId());
	}

	/**
	 * Find external identifier of internal object
	 *
	 * @param obj			   Object to get external identifier of
	 * @param sd DataSourceDescription to get
	 * @return External id that if found, or <code>null</code> otherwise
	 */
	@Nullable
    @Override
	public <T extends DomainObject> String getExternalId(@NotNull T obj, Stub<DataSourceDescription> sd) {
		return correctionDaoExt.getExternalId(obj.getId(), typeRegistry.getType(obj.getClass()), sd.getId());
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        correctionDao.setJpaTemplate(jpaTemplate);
        correctionDaoExt.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setTypeRegistry(ClassToTypeRegistry typeRegistry) {
		this.typeRegistry = typeRegistry;
	}

	@Required
	public void setCorrectionDaoExt(CorrectionDaoExt correctionDaoExt) {
		this.correctionDaoExt = correctionDaoExt;
	}

    @Required
    public void setCorrectionDao(CorrectionDao correctionDao) {
        this.correctionDao = correctionDao;
    }

}
