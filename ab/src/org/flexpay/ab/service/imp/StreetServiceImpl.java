package org.flexpay.ab.service.imp;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.dao.*;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.NameTimeDependentDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Pair;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.service.imp.NameTimeDependentServiceImpl;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class StreetServiceImpl extends NameTimeDependentServiceImpl<
		StreetNameTranslation, StreetName, StreetNameTemporal, Street, Town>
		implements StreetService {

	private StreetDao streetDao;
	private StreetDaoExt streetDaoExt;
	private StreetNameDao streetNameDao;
	private StreetNameTemporalDao streetNameTemporalDao;
	private StreetNameTranslationDao streetNameTranslationDao;
	private StreetTypeTemporalDao streetTypeTemporalDao;
	private TownDao townDao;

	private ParentService<TownFilter> parentService;


	/**
	 * Setter for property 'streetDao'.
	 *
	 * @param streetDao Value to set for property 'streetDao'.
	 */
	public void setStreetDao(StreetDao streetDao) {
		this.streetDao = streetDao;
	}

	public void setStreetDaoExt(StreetDaoExt streetDaoExt) {
		this.streetDaoExt = streetDaoExt;
	}

	/**
	 * Setter for property 'streetNameDao'.
	 *
	 * @param streetNameDao Value to set for property 'streetNameDao'.
	 */
	public void setStreetNameDao(StreetNameDao streetNameDao) {
		this.streetNameDao = streetNameDao;
	}

	/**
	 * Setter for property 'streetNameTemporalDao'.
	 *
	 * @param streetNameTemporalDao Value to set for property 'streetNameTemporalDao'.
	 */
	public void setStreetNameTemporalDao(StreetNameTemporalDao streetNameTemporalDao) {
		this.streetNameTemporalDao = streetNameTemporalDao;
	}

	/**
	 * Setter for property 'streetNameTranslationDao'.
	 *
	 * @param streetNameTranslationDao Value to set for property 'streetNameTranslationDao'.
	 */
	public void setStreetNameTranslationDao(StreetNameTranslationDao streetNameTranslationDao) {
		this.streetNameTranslationDao = streetNameTranslationDao;
	}

	/**
	 * Setter for property 'townDao'.
	 *
	 * @param townDao Value to set for property 'townDao'.
	 */
	public void setTownDao(TownDao townDao) {
		this.townDao = townDao;
	}

	/**
	 * Setter for property 'parentService'.
	 *
	 * @param parentService Value to set for property 'parentService'.
	 */
	public void setParentService(ParentService<TownFilter> parentService) {
		this.parentService = parentService;
	}

	public void setStreetTypeTemporalDao(StreetTypeTemporalDao streetTypeTemporalDao) {
		this.streetTypeTemporalDao = streetTypeTemporalDao;
	}

	/**
	 * Get DAO implementation working with Name time-dependent objects
	 *
	 * @return GenericDao implementation
	 */
	protected NameTimeDependentDao<Street, Long> getNameTimeDependentDao() {
		return streetDao;
	}

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	protected GenericDao<StreetNameTemporal, Long> getNameTemporalDao() {
		return streetNameTemporalDao;
	}

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	protected GenericDao<StreetName, Long> getNameValueDao() {
		return streetNameDao;
	}

	/**
	 * Get DAO implementation working with name translations
	 *
	 * @return GenericDao implementation
	 */
	protected GenericDao<StreetNameTranslation, Long> getNameTranslationDao() {
		return streetNameTranslationDao;
	}

	/**
	 * Get DAO implementation working with parent objects
	 *
	 * @return GenericDao implementation
	 */
	protected GenericDao<Town, Long> getParentDao() {
		return townDao;
	}

	/**
	 * Getter for property 'newNameTemporal'.
	 *
	 * @return Value for property 'newNameTemporal'.
	 */
	protected StreetNameTemporal getNewNameTemporal() {
		return new StreetNameTemporal();
	}

	/**
	 * Getter for property 'newNameTimeDependent'.
	 *
	 * @return Value for property 'newNameTimeDependent'.
	 */
	protected Street getNewNameTimeDependent() {
		return new Street();
	}

	/**
	 * Getter for property 'emptyName'.
	 *
	 * @return Value for property 'emptyName'.
	 */
	protected StreetName getEmptyName() {
		return new StreetName();
	}

	/**
	 * Check if disable operation on object is allowed
	 *
	 * @param street	Name time dependent object
	 * @param container Exceptions container to add exception for
	 * @return <code>true</code> if operation allowed, or <code>false</otherwise>
	 */
	protected boolean canDisable(Street street, FlexPayExceptionContainer container) {
		return true;
	}

	/**
	 * return base for name time-dependent objects in i18n files, like 'region', 'town', etc.
	 *
	 * @return Localization key base
	 */
	protected String getI18nKeyBase() {
		return "ab.street";
	}

	/**
	 * Create empty name translation
	 *
	 * @return name translation
	 */
	public StreetNameTranslation getEmptyNameTranslation() {
		return new StreetNameTranslation();
	}

	/**
	 * Save street districts
	 *
	 * @param street	Street to save districts for
	 * @param objectIds List of district ids
	 * @return saved street object
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public Street saveDistricts(Street street, Set<Long> objectIds) {
		Set<District> districts = new HashSet<District>();
		for (Long id : objectIds) {
			District district = new District();
			district.setId(id);
			districts.add(district);
		}
		street.setDistricts(districts);
		streetDao.update(street);
		return street;
	}

	/**
	 * {@inheritDoc}
	 */
	public StreetFilter initFilter(StreetFilter parentFilter, PrimaryKeyFilter forefatherFilter, Locale locale)
			throws FlexPayException {

		if (parentFilter == null) {
			parentFilter = new StreetFilter();
		}

		parentFilter.setNames(getTranslations(forefatherFilter, locale));

		Collection<StreetNameTranslation> names = parentFilter.getNames();
		if (names.isEmpty()) {
			throw new FlexPayException("No street names", "ab.no_streets");
		}
		if (parentFilter.getSelectedId() == null || !isFilterValid(parentFilter)) {
			StreetName firstObject = (StreetName) names.iterator().next().getTranslatable();
			parentFilter.setSelectedId(firstObject.getObject().getId());
		}

		return parentFilter;
	}

	private boolean isFilterValid(StreetFilter filter) {
		for (StreetNameTranslation nameTranslation : filter.getNames()) {
			StreetName name = (StreetName) nameTranslation.getTranslatable();
			if (name.getStub().getId().equals(filter.getSelectedId())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public ArrayStack initFilters(ArrayStack filters, Locale locale) throws FlexPayException {
		if (filters == null) {
			filters = new ArrayStack();
		}

		StreetFilter parentFilter = filters.isEmpty() ? null : (StreetFilter) filters.pop();
		filters = parentService.initFilters(filters, locale);
		TownFilter forefatherFilter = (TownFilter) filters.peek();

		// init filter
		parentFilter = initFilter(parentFilter, forefatherFilter, locale);
		filters.push(parentFilter);

		return filters;
	}

	@Transactional (readOnly = true)
	public List<Street> findByTownAndName(@NotNull Stub<Town> stub, @NotNull String name) {
		return streetDao.findByTownAndName(stub.getId(), name);
	}

	/**
	 * Save Street types timeline
	 *
	 * @param object Street to update
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public void saveTypes(Street object) {

		if (log.isDebugEnabled()) {
			log.debug("Types to save: " + object.getTypeTemporals());
		}

		streetDaoExt.invalidateTypeTemporals(object.getId(), ApplicationConfig.getFutureInfinite(), DateUtil.now());

		for (StreetTypeTemporal temporal : object.getTypeTemporals()) {
			if (temporal.getId() != null) {
				streetTypeTemporalDao.update(temporal);
			} else {
				streetTypeTemporalDao.create(temporal);
			}
		}
	}

	public String format(@NotNull Stub<Street> stub, @NotNull Locale locale, boolean shortMode) throws FlexPayException {
		Street street = streetDao.read(stub.getId());
		if (street == null) {
			throw new FlexPayException("Invalid id", "error.invalid_id");
		}
		return street.format(locale, shortMode);
	}

	/**
	 * Get street and name pair
	 *
	 * @param stub Street stub
	 * @return Street and street name pair
	 */
	public Pair<Street, String> getFullStreetName(Stub<Street> stub) {
		return null;
	}
}
