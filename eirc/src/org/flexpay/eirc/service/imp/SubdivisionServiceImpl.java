package org.flexpay.eirc.service.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.internal.SessionUtils;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.CollectionUtils.set;
import org.flexpay.eirc.dao.SubdivisionDao;
import org.flexpay.eirc.persistence.Organisation;
import org.flexpay.eirc.persistence.Subdivision;
import org.flexpay.eirc.persistence.SubdivisionDescription;
import org.flexpay.eirc.persistence.SubdivisionName;
import org.flexpay.eirc.persistence.filters.SubdivisionFilter;
import org.flexpay.eirc.service.OrganisationService;
import org.flexpay.eirc.service.SubdivisionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Transactional (readOnly = true)
public class SubdivisionServiceImpl implements SubdivisionService {

	private OrganisationService organisationService;
	private SubdivisionDao subdivisionDao;
	private SessionUtils sessionUtils;

	/**
	 * Disable subdivisions.
	 * <p/>
	 * TODO: disable child subdivisions and juridical persons as well
	 *
	 * @param objectIds Organisations identifiers to disable
	 */
	@Transactional (readOnly = false)
	public void disable(@NotNull Set<Long> objectIds) {
		for (Long id : objectIds) {
			Subdivision subdivision = subdivisionDao.read(id);
			if (subdivision != null) {
				subdivision.disable();
				subdivisionDao.update(subdivision);
			}
		}
	}

	/**
	 * Read organisation info with subdivisions
	 *
	 * @param stub Organisation stub
	 * @return Organisation
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if stub references invalid object
	 */
	@NotNull
	public List<Subdivision> getOrganisationSubdivisions(@NotNull Stub<Organisation> stub) throws FlexPayException {
		Organisation org = organisationService.read(new Organisation(stub));
		if (org == null) {
			throw new FlexPayException("Invalid id", "error.invalid_id");
		}

		return subdivisionDao.findSubdivisions(stub.getId(), stub.getId());
	}

	/**
	 * Read full Subdivision info
	 *
	 * @param stub Subdivision stub
	 * @return Bank
	 */
	public Subdivision read(@NotNull Subdivision stub) {
		if (stub.isNotNew()) {
			return subdivisionDao.readFull(new Stub<Subdivision>(stub).getId());
		}

		return new Subdivision(0L);
	}

	/**
	 * Save or update subdivision
	 *
	 * @param subdivision Subdivision to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	public void save(@NotNull Subdivision subdivision) throws FlexPayExceptionContainer {
		validate(subdivision);
		if (subdivision.isNew()) {
			subdivision.setId(null);
			subdivisionDao.create(subdivision);
		} else {
			subdivisionDao.update(subdivision);
		}
		updateTreePaths(stub(subdivision.getHeadOrganisation()));
	}

	@Transactional (readOnly = false)
	private void updateTreePaths(@NotNull Stub<Organisation> head) {
		List<Subdivision> subdivisions = subdivisionDao.findSubdivisions(head.getId(), -1L);
		Map<Long, Subdivision> id2Subs = getId2Subdivisions(subdivisions);

		// run update of paths for all subdivisions
		for (Subdivision subdivision : subdivisions) {
			String actualPath = calculateTreePath(subdivision, id2Subs);
			if (!actualPath.equals(subdivision.getTreePath())) {
				subdivision.setTreePath(actualPath);
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

		if (subdivision.hasNoHeadOrganisation()) {
			container.addException(new FlexPayException(
					"No organisation", "eirc.error.subdivision.no_organisation"));
		}
		Organisation organisation = organisationService.getOrganisation(stub(subdivision.getHeadOrganisation()));
		//noinspection ConstantConditions
		subdivision.setHeadOrganisation(organisation);

		Organisation juridicalPersonStub = subdivision.getJuridicalPerson();
		if (juridicalPersonStub != null) {
			Organisation juridicalPerson = organisationService.getOrganisation(stub(juridicalPersonStub));
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

		// check same head organisation
		if (!parent.getHeadOrganisation().equals(subdivision.getHeadOrganisation())) {
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
		Stub<Organisation> headOrganisation = stub(subdivision.getHeadOrganisation());
		List<Subdivision> subdivisions = subdivisionDao.findSubdivisions(headOrganisation.getId(), -1L);
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
	 * @param stub			  Organisation that departments to put to filter
	 */
	public void initFilter(@NotNull SubdivisionFilter subdivisionFilter, @NotNull Stub<Organisation> stub) {
		List<Subdivision> subdivisions = subdivisionDao.findSubdivisions(stub.getId(), -1L);
		subdivisionFilter.setSubdivisions(subdivisions);
	}

	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}

	public void setSubdivisionDao(SubdivisionDao subdivisionDao) {
		this.subdivisionDao = subdivisionDao;
	}

	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}
}
