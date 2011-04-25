package org.flexpay.orgs.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.orgs.dao.OrganisationInstanceDao;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.OrganizationInstance;
import org.flexpay.orgs.persistence.OrganizationInstanceDescription;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.service.OrganizationInstanceService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;

@Transactional (readOnly = true)
public abstract class OrganizationInstanceServiceImpl<
		D extends OrganizationInstanceDescription,
		T extends OrganizationInstance<D, T>>
		implements OrganizationInstanceService<D, T> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private SessionUtils sessionUtils;
	protected OrganisationInstanceDao<D, T> instanceDao;

	private ModificationListener<T> modificationListener;

	/**
	 * List registered instances
	 *
	 * @param pager Page
	 * @return List of registered instances
	 */
	@NotNull
	public List<T> listInstances(@NotNull Page<T> pager) {
		return instanceDao.findInstances(pager);
	}

    /**
	 * List registered instances
	 *
        * @param range Fetch range
        * @return List of registered instances
	 */
	@NotNull
	public List<T> listInstances(@NotNull FetchRange range) {
		return instanceDao.listInstancesWithIdentities(range);
	}

	/**
	 * Read full instance info
	 *
	 * @param stub Instance stub
	 * @return Instance if found, or <code>null</code> otherwise
	 */
	@SuppressWarnings ({"unchecked"})
	public <SubT extends T> SubT read(@NotNull Stub<SubT> stub) {
		return (SubT) instanceDao.readFull(stub.getId());
	}

	/**
	 * Disable instances
	 *
	 * @param objectIds Instances identifiers to disable
	 */
	@Transactional (readOnly = false)
	public void disable(@NotNull Set<Long> objectIds) {
		for (Long id : objectIds) {
			T t = instanceDao.read(id);
			if (t != null) {
				t.disable();
				instanceDao.update(t);

				modificationListener.onDelete(t);
				log.debug("Disabled instance: {}", t);
			}
		}
	}

	/**
	 * Create instance
	 *
	 * @param instance Organisation instance to save
	 * @return saved instance back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@NotNull
	@Transactional (readOnly = false)
	public T create(@NotNull T instance) throws FlexPayExceptionContainer {
		validate(instance);

		if (instance.isNotNew()) {
			throw new FlexPayExceptionContainer(new FlexPayException("Not new", "common.error.create_saved"));
		}

		// id=0 is also a new object
		instance.setId(null);
		instanceDao.create(instance);

		modificationListener.onCreate(instance);

		return instance;
	}

	/**
	 * Update instance
	 *
	 * @param instance Organisation instance to save
	 * @return updated instance back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@NotNull
	@Transactional (readOnly = false)
	public T update(@NotNull T instance) throws FlexPayExceptionContainer {
		validate(instance);

		if (instance.isNew()) {
			throw new FlexPayExceptionContainer(new FlexPayException("New", "common.error.update_new"));
		}

		T old = read(stub(instance));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No object found to update " + instance));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, instance);

		instanceDao.update(instance);

		return instance;
	}

	/**
	 * Get I18n error code for error: found several instances
	 *
	 * @return error code
	 */
	protected abstract String getSeveralInstancesErrorCode();

	/**
	 * Get I18n error code for error: found instance of this type
	 *
	 * @return error code
	 */
	protected abstract String getInstanceExistsErrorCode();

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(T instance) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		// check if default language description specified
		boolean defaultDescFound = false;
		for (D description : instance.getDescriptions()) {
			if (description.getLang().isDefault() && StringUtils.isNotBlank(description.getName())) {
				defaultDescFound = true;
			}
		}
		if (!defaultDescFound) {
			ex.addException(new FlexPayException(
					"No default desc", "orgs.error.orginstance.no_default_description"));
		}

		// check if there is only one instance of this type for organization
		List<T> instances = instanceDao.findOrganizationInstances(instance.getOrganizationStub().getId());
		if (instances.size() > 1) {
			ex.addException(new FlexPayException("Several instances", getSeveralInstancesErrorCode()));
		}
		if (!instances.isEmpty()) {
			T t = instances.get(0);
			if (!t.equals(instance)) {
				ex.addException(new FlexPayException("Instance already exists", getInstanceExistsErrorCode()));
			}
		}
		sessionUtils.evict(instances);

		// let child do some validation
		doValidate(instance, ex);

		if (ex.isNotEmpty()) {
			throw ex;
		}
	}

	/**
	 * Initialize organizations filter, includes only organizations that are not instances of type <code>T</code> or this
	 * particular <code>instance</code> organization
	 *
	 * @param filter   Filter to initialize
	 * @param instance Organisation Instance
	 * @return filter
	 */
	@NotNull
	public OrganizationFilter initInstancelessFilter(@NotNull OrganizationFilter filter, @NotNull T instance) {

		@SuppressWarnings ({"UnnecessaryBoxing"})
		Long includedId = instance.isNotNew() ? instance.getId() : Long.valueOf(-1L);
		List<Organization> organizations = instanceDao.findInstancelessOrganizations(includedId);
		filter.setOrganizations(organizations);

		return filter;
	}

	/**
	 * Test method that deletes created instance
	 *
	 * @param instance Organization instance to delete
	 */
	@Override
	@Transactional (readOnly = false)
	public void delete(@NotNull T instance) {
		instanceDao.delete(instance);
	}

	/**
	 * Do any specific validation if necessary
	 *
	 * @param instance Instance to validate
	 * @param ex	   Container to store validation errors in
	 */
	protected abstract void doValidate(T instance, FlexPayExceptionContainer ex);

	@Required
	public void setInstanceDao(OrganisationInstanceDao<D, T> instanceDao) {
		this.instanceDao = instanceDao;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<T> modificationListener) {
		this.modificationListener = modificationListener;
	}
}
