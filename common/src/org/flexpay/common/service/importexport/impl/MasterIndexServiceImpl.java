package org.flexpay.common.service.importexport.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.DataSourceDescriptionDao;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

@Transactional
public class MasterIndexServiceImpl implements MasterIndexService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private CorrectionsService correctionsService;
	private DataSourceDescriptionDao sourceDescriptionDao;

	private Stub<DataSourceDescription> cachedDescriptionStub;

	/**
	 * Get next master index value
	 *
	 * @param obj Object to get next index for
	 * @return next index value
	 */
	@Nullable
    @Override
	public <T extends DomainObject> String getNewMasterIndex(@NotNull T obj) {

		assertNotNew(obj);
		String instanceId = ApplicationConfig.getInstanceId();
		return StringUtils.isNotBlank(instanceId) ? instanceId + "-" + obj.getId() : null;
	}

	private <T extends DomainObject> void assertNotNew(T obj) {

		Long id = obj.getId();
		if (id == null || id <= 0L) {
			throw new IllegalArgumentException("No new object allowed for master index request");
		}
	}

	/**
	 * Find master index of internal object
	 *
	 * @param obj internal object to find index for
	 * @return master index value if available, or <code>null</code> otherwise
	 */
	@Nullable
    @Override
	public <T extends DomainObject> String getMasterIndex(@NotNull T obj) {

		assertNotNew(obj);

		return correctionsService.getExternalId(obj, getMasterSourceDescriptionStub());
	}

	/**
	 * Get a reference to master data source description
	 *
	 * @return DataSourceDescription
	 */
	@NotNull
    @Override
	public Stub<DataSourceDescription> getMasterSourceDescriptionStub() {
		if (cachedDescriptionStub != null) {
			return cachedDescriptionStub;
		}
		log.info("Reading master source description");
		List<DataSourceDescription> descriptions = sourceDescriptionDao.findMasterSourceDescription();
		if (descriptions.isEmpty()) {
			throw new IllegalStateException("Master index data source not found");
		}
		if (descriptions.size() > 1) {
			throw new IllegalStateException("Several master indexes found");
		}
		cachedDescriptionStub = stub(descriptions.get(0));
		return cachedDescriptionStub;
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        sourceDescriptionDao.setJpaTemplate(jpaTemplate);
        correctionsService.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setSourceDescriptionDao(DataSourceDescriptionDao sourceDescriptionDao) {
		this.sourceDescriptionDao = sourceDescriptionDao;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

}
