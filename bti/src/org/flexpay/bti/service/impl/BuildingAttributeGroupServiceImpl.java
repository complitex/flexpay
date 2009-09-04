package org.flexpay.bti.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.bti.dao.BuildingAttributeGroupDao;
import org.flexpay.bti.persistence.building.BuildingAttributeGroup;
import org.flexpay.bti.persistence.building.BuildingAttributeGroupName;
import org.flexpay.bti.persistence.filters.BuildingAttributeGroupFilter;
import org.flexpay.bti.service.BuildingAttributeGroupService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.internal.SessionUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Transactional (readOnly = true)
public class BuildingAttributeGroupServiceImpl implements BuildingAttributeGroupService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private BuildingAttributeGroupDao groupDao;
	private SessionUtils sessionUtils;
	private ModificationListener<BuildingAttributeGroup> modificationListener;

	/**
	 * Read full group info
	 *
	 * @param stub group stub
	 * @return group if found, or <code>null</code> if not found
	 */
	@Override
	public BuildingAttributeGroup readFull(@NotNull Stub<BuildingAttributeGroup> stub) {
		log.debug("Reading group {}", stub);
		return groupDao.readFull(stub.getId());
	}

	@Override
	public List<BuildingAttributeGroup> readFullGroups(@NotNull Collection<Long> ids) {
		return groupDao.readFullCollection(ids, false);
	}

	/**
	 * List groups
	 *
	 * @param pager Group pager
	 * @return List of all available
	 */
	@Override
	public List<BuildingAttributeGroup> listGroups(Page<BuildingAttributeGroup> pager) {
		return groupDao.findGroups(pager);
	}

	/**
	 * List groups
	 *
	 * @return List of all available
	 */
	@Override
	public List<BuildingAttributeGroup> listGroups() {
		return groupDao.findAllGroups();
	}

	/**
	 * Initialize filter
	 *
	 * @param filter Group filter to init
	 * @return filter back
	 */
	@NotNull
	@Override
	public BuildingAttributeGroupFilter initFilter(@NotNull BuildingAttributeGroupFilter filter) {
		filter.setGroups(listGroups());
		return filter;
	}

	/**
	 * Create attribute group
	 *
	 * @param group Attribute group to persist
	 * @return Saved group back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@NotNull
	@Override
	@Transactional (readOnly = false)
	public BuildingAttributeGroup create(@NotNull BuildingAttributeGroup group) throws FlexPayExceptionContainer {

		validate(group);
		group.setId(null);
		groupDao.create(group);
		modificationListener.onCreate(group);
		return group;
	}

	/**
	 * Update attribute group
	 *
	 * @param group Attribute group to persist
	 * @return Saved group back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@NotNull
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@Override
	public BuildingAttributeGroup update(@NotNull BuildingAttributeGroup group) throws FlexPayExceptionContainer {

		validate(group);
		if (group.isNew()) {
			throw new FlexPayExceptionContainer(new FlexPayException("New", "common.error.update_new"));
		}

		BuildingAttributeGroup old = readFull(Stub.stub(group));
		if (old == null) {
			throw new FlexPayExceptionContainer(new FlexPayException("No object found to update " + group));
		}

		sessionUtils.evict(old);
		modificationListener.onUpdate(old, group);

		groupDao.update(group);

		return group;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(BuildingAttributeGroup group) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		boolean defaultNameFound = false;
		for (BuildingAttributeGroupName translation : group.getTranslations()) {
			boolean nameBlank = StringUtils.isBlank(translation.getName());
			if (translation.getLang().isDefault() && !nameBlank) {
				defaultNameFound = true;
			}
		}
		if (!defaultNameFound) {
			ex.addException(new FlexPayException(
					"No default name", "bti.error.building.attribute.group.no_default_lang_name"));
		}


		if (ex.isNotEmpty()) {
			throw ex;
		}
	}

	/**
	 * Disable attribute group
	 *
	 * @param ids Attribute group keys to disable
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	@Override
	public void disable(@NotNull Collection<Long> ids) throws FlexPayExceptionContainer {
		for (Long id : ids) {
			if (id == null || id <= 0) {
				continue;
			}
			BuildingAttributeGroup group = readFull(new Stub<BuildingAttributeGroup>(id));
			if (group != null) {
				group.disable();
				groupDao.update(group);

				modificationListener.onDelete(group);
				log.debug("Disabled object: {}", group);
			}
		}
	}

	@Required
	public void setGroupDao(BuildingAttributeGroupDao groupDao) {
		this.groupDao = groupDao;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<BuildingAttributeGroup> modificationListener) {
		this.modificationListener = modificationListener;
	}

}
