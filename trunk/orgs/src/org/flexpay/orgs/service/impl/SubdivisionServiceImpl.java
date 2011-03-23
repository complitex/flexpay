package org.flexpay.orgs.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.orgs.dao.SubdivisionDao;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.Subdivision;
import org.flexpay.orgs.persistence.SubdivisionDescription;
import org.flexpay.orgs.persistence.SubdivisionName;
import org.flexpay.orgs.persistence.filters.SubdivisionFilter;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.SubdivisionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.CollectionUtils.set;

@Transactional (readOnly = true)
public class SubdivisionServiceImpl implements SubdivisionService {

	private OrganizationService organizationService;
	private SubdivisionDao subdivisionDao;
	private SessionUtils sessionUtils;
	private ModificationListener<Subdivision> modificationListener;

	/**
	 * Disable subdivisions.
	 * <p/>
	 * TODO: disable child subdivisions and juridical persons as well
	 *
	 * @param objectIds Organizations identifiers to disable
	 */
	@Transactional (readOnly = false)
	public void disable(@NotNull Set<Long> objectIds) {
		for (Long id : objectIds) {
			Subdivision subdivision = subdivisionDao.read(id);
			if (subdivision != null) {
				subdivision.disable();
				subdivisionDao.update(subdivision);

				modificationListener.onDelete(subdivision);
			}
		}
	}

	/**
	 * Read organization info with subdivisions
	 *
	 * @param stub Organization stub
	 * @return Organization
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if stub references invalid object
	 */
	@NotNull
	public List<Subdivision> getOrganizationSubdivisions(@NotNull Stub<Organization> stub) throws FlexPayException {
		Organization org = organizationService.readFull(stub);
		if (org == null) {
			throw new FlexPayException("Invalid id", "common.error.invalid_id");
		}

		return subdivisionDao.findSubdivisions(stub.getId(), stub.getId());
	}

	/**
	 * Read full Subdivision info
	 *
	 * @param stub Subdivision stub
	 * @return Bank
	 */
	public Subdivision read(@NotNull Stub<Subdivision> stub) {
		return subdivisionDao.readFull(stub.getId());
	}

	/**
	 * Read full subdivisions info by their unique ids
	 *
	 * @param ids		   Subdivision ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Subdivision objects found
	 */
	@Override
	public List<Subdivision> readFull(Collection<Long> ids, boolean preserveOrder) {
		return subdivisionDao.readFullCollection(ids, preserveOrder);
	}

	/**
	 * Save or update subdivision
	 *
	 * @param subdivision Subdivision to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	public void create(@NotNull Subdivision subdivision) throws FlexPayExceptionContainer {
		validate(subdivision);
		subdivision.setId(null);
		subdivisionDao.create(subdivision);

		modificationListener.onCreate(subdivision);

		updateTreePaths(stub(subdivision.getHeadOrganization()));
	}

	/**
	 * Save or update subdivision
	 *
	 * @param subdivision Subdivision to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	public void update(@NotNull Subdivision subdivision) throws FlexPayExceptionContainer {

		validate(subdivision);

		Subdivision old = read(Stub.stub(subdivision));
		if (old == null) {
			throw new FlexPayExceptionContainer(new FlexPayException("No object found to update " + subdivision));
		}

		sessionUtils.evict(old);
		modificationListener.onUpdate(old, subdivision);

		subdivisionDao.update(subdivision);
		updateTreePaths(stub(subdivision.getHeadOrganization()));
	}

	@Transactional (readOnly = false)
	private void updateTreePaths(@NotNull Stub<Organization> head) {
		List<Subdivision> subdivisions = subdivisionDao.findSubdivisions(head.getId(), -1L);
		Map<Long, Subdivision> id2Subs = getId2Subdivisions(subdivisions);

		// run update of paths for all subdivisions
		for (Subdivision subdivision : subdivisions) {
			String actualPath = calculateTreePath(subdivision, id2Subs);
			if (!actualPath.equals(subdivision.getTreePath())) {
				Subdivision old = read(Stub.stub(subdivision));
				sessionUtils.evict(old);
				subdivision.setTreePath(actualPath);

				if (old != null) {
					modificationListener.onUpdate(old, subdivision);
				}

				subdivisionDao.update(subdivision);
			}
		}
	}

	@NotNull
	private String calculateTreePath(@NotNull Subdivision subdivision, @NotNull Map<Long, Subdivision> id2Subs) {

		StringBuilder path = new StringBuilder(".");
		Subdivision parent = subdivision;
		while (parent != null) {
			path.insert(0, parent.getId()).insert(0, ".");

			// prevent unnecessary lazy fetch
			parent = id2Subs.get(parent.getId());
			parent = parent.getParentSubdivision();
		}

		return path.toString();
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(Subdivision subdivision) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (subdivision.hasNoHeadOrganization()) {
			container.addException(new FlexPayException(
					"No organization", "eirc.error.subdivision.no_organization"));
		}
		Organization organization = organizationService.readFull(stub(subdivision.getHeadOrganization()));
		//noinspection ConstantConditions
		subdivision.setHeadOrganization(organization);

		Organization juridicalPersonStub = subdivision.getJuridicalPerson();
		if (juridicalPersonStub != null) {
			Organization juridicalPerson = organizationService.readFull(stub(juridicalPersonStub));
			if (juridicalPerson == null) {
				container.addException(new FlexPayException(
						"Invalid juridical person", "eirc.error.subdivision.invalid_juridical_person"));
			} else {
				subdivision.setJuridicalPerson(juridicalPerson);
			}
		}

		if (StringUtils.isBlank(subdivision.getRealAddress())) {
			container.addException(new FlexPayException(
					"No real address", "eirc.error.subdivision.no_real_address"));
		}

		boolean defaultNameFound = false;
		for (SubdivisionName description : subdivision.getNames()) {
			if (description.getLang().isDefault() && StringUtils.isNotBlank(description.getName())) {
				defaultNameFound = true;
			}
		}
		if (!defaultNameFound) {
			container.addException(new FlexPayException(
					"No default lang name", "eirc.error.subdivision.no_default_lang_name"));
		}

		boolean defaultDescFound = false;
		for (SubdivisionDescription description : subdivision.getDescriptions()) {
			if (description.getLang().isDefault() && StringUtils.isNotBlank(description.getName())) {
				defaultDescFound = true;
			}
		}
		if (!defaultDescFound) {
			container.addException(new FlexPayException(
					"No default lang desc", "eirc.error.subdivision.no_default_lang_description"));
		}

		validateParentSubdivision(subdivision, container);

		if (container.isNotEmpty()) {
			throw container;
		}
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validateParentSubdivision(Subdivision subdivision, FlexPayExceptionContainer container) {
		// check parent subdivision
		Subdivision parent = subdivision.getParentSubdivision();
		if (parent == null) {
			return;
		}

		parent = subdivisionDao.read(stub(parent).getId());
		if (parent == null) {
			container.addException(new FlexPayException(
					"Invalid parent id", "eirc.error.subdivision.invalid_parent_id"));
			return;
		}

		// check same head organization
		if (!parent.getHeadOrganization().equals(subdivision.getHeadOrganization())) {
			container.addException(new FlexPayException(
					"Invalid parent subdivision", "eirc.error.subdivision.parent_head_mismatch"));
			return;
		}

		if (hasCircularDependence(subdivision)) {
			container.addException(new FlexPayException(
					"Circular dependence", "eirc.error.subdivision.circular_dependence"));
			return;
		}

		subdivision.setParentSubdivision(parent);
	}

	private boolean hasCircularDependence(Subdivision subdivision) {
		Stub<Organization> headOrganization = stub(subdivision.getHeadOrganization());
		List<Subdivision> subdivisions = subdivisionDao.findSubdivisions(headOrganization.getId(), -1L);
		Map<Long, Subdivision> id2Subdivisions = getId2Subdivisions(subdivisions);

		try {
			Set<Long> foundParents = set(subdivision.getId());
			Subdivision parent = subdivision.getParentSubdivision();
			while (parent != null) {
				if (foundParents.contains(parent.getId())) {
					return true;
				}
				foundParents.add(parent.getId());
				parent = parent.getParentSubdivision();
				if (parent != null) {
					// prevent annecessary lazy traversal
					parent = id2Subdivisions.get(parent.getId());
				}
			}
		} finally {
			sessionUtils.evict(subdivisions);
		}

		return false;
	}

	private Map<Long, Subdivision> getId2Subdivisions(List<Subdivision> subdivisions) {
		Map<Long, Subdivision> id2Subdivisions = map();
		for (Subdivision persistent : subdivisions) {
			id2Subdivisions.put(persistent.getId(), persistent);
		}
		return id2Subdivisions;
	}

	/**
	 * Initialize subdivision filter
	 *
	 * @param subdivisionFilter Filter to initialize
	 * @param stub			  Organization that departments to put to filter
	 */
	public void initFilter(@NotNull SubdivisionFilter subdivisionFilter, @NotNull Stub<Organization> stub) {
		List<Subdivision> subdivisions = subdivisionDao.findSubdivisions(stub.getId(), -1L);
		subdivisionFilter.setSubdivisions(subdivisions);
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setSubdivisionDao(SubdivisionDao subdivisionDao) {
		this.subdivisionDao = subdivisionDao;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<Subdivision> modificationListener) {
		this.modificationListener = modificationListener;
	}
}
