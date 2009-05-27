package org.flexpay.common.service.importexport.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.DataSourceDescriptionDao;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class MasterIndexServiceImpl implements MasterIndexService {

	private boolean useSelfInstanceIfNotFound = true;
	private CorrectionsService correctionsService;
	private DataSourceDescriptionDao sourceDescriptionDao;

	/**
	 * Get next master index value
	 *
	 * @param obj Object to get next index for
	 * @return next index value
	 */
	@Nullable
	public <T extends DomainObject> String getNewMasterIndex(@NotNull T obj) {

		if (obj.isNew()) {
			throw new IllegalArgumentException("No new object allowed for master index request");
		}
		String instanceId = ApplicationConfig.getInstanceId();
		return StringUtils.isNotBlank(instanceId) ? instanceId + "-" + obj.getId() : null;
	}

	/**
	 * Find master index of internal object
	 *
	 * @param obj internal object to find index for
	 * @return master index value if available, or <code>null</code> otherwise
	 */
	@Nullable
	public <T extends DomainObject> String getMasterIndex(@NotNull T obj) {
		Long id = obj.getId();
		if (id == null || id <= 0) {
			throw new IllegalArgumentException("No new object allowed for master index request");
		}

		String index = correctionsService.getExternalId(obj, getMasterSourceDescription());

		if (index == null && useSelfInstanceIfNotFound) {
			index = getNewMasterIndex(obj);
		}

		return index;
	}

	/**
	 * Get a reference to master data source description
	 *
	 * @return DataSourceDescription
	 */
	@NotNull
	public DataSourceDescription getMasterSourceDescription() {
		List<DataSourceDescription> descriptions = sourceDescriptionDao.findMasterSourceDescription();
		if (descriptions.isEmpty()) {
			throw new IllegalStateException("Master index data source not found");
		}
		if (descriptions.size() > 1) {
			throw new IllegalStateException("Several master indexes found");
		}
		return descriptions.get(0);
	}

	@Required
	public void setSourceDescriptionDao(DataSourceDescriptionDao sourceDescriptionDao) {
		this.sourceDescriptionDao = sourceDescriptionDao;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	// TODO: remove this dirty hack when sync is properly configured
	public void setUseSelfInstanceIfNotFound(boolean useSelfInstanceIfNotFound) {
		this.useSelfInstanceIfNotFound = useSelfInstanceIfNotFound;
	}
}
