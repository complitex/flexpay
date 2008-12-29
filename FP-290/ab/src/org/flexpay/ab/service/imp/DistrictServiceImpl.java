package org.flexpay.ab.service.imp;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.*;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.DistrictFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.NameTimeDependentDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.service.imp.NameTimeDependentServiceImpl;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Locale;

/**
 * Class DistrictServiceImpl
 */
@Transactional (readOnly = true)
public class DistrictServiceImpl extends
		NameTimeDependentServiceImpl<DistrictNameTranslation, DistrictName, DistrictNameTemporal, District, Town>
		implements DistrictService {

	private DistrictDao districtDao;
	private DistrictNameDao districtNameDao;
	private DistrictNameTemporalDao districtNameTemporalDao;
	private DistrictNameTranslationDao districtNameTranslationDao;
	private TownDao townDao;

	private ParentService<TownFilter> parentService;

	/**
	 * Setter for property 'districtDao'.
	 *
	 * @param districtDao Value to set for property 'districtDao'.
	 */
	public void setDistrictDao(DistrictDao districtDao) {
		this.districtDao = districtDao;
	}

	/**
	 * Setter for property 'districtNameDao'.
	 *
	 * @param districtNameDao Value to set for property 'districtNameDao'.
	 */
	public void setDistrictNameDao(DistrictNameDao districtNameDao) {
		this.districtNameDao = districtNameDao;
	}

	/**
	 * Setter for property 'districtNameTemporalDao'.
	 *
	 * @param districtNameTemporalDao Value to set for property 'districtNameTemporalDao'.
	 */
	public void setDistrictNameTemporalDao(
			DistrictNameTemporalDao districtNameTemporalDao) {
		this.districtNameTemporalDao = districtNameTemporalDao;
	}

	/**
	 * Setter for property 'districtNameTranslationDao'.
	 *
	 * @param districtNameTranslationDao Value to set for property 'districtNameTranslationDao'.
	 */
	public void setDistrictNameTranslationDao(
			DistrictNameTranslationDao districtNameTranslationDao) {
		this.districtNameTranslationDao = districtNameTranslationDao;
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
	 * Get DAO implementation working with Name time-dependent objects
	 *
	 * @return GenericDao implementation
	 */
	protected NameTimeDependentDao<District, Long> getNameTimeDependentDao() {
		return districtDao;
	}

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	protected GenericDao<DistrictNameTemporal, Long> getNameTemporalDao() {
		return districtNameTemporalDao;
	}

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	protected GenericDao<DistrictName, Long> getNameValueDao() {
		return districtNameDao;
	}

	/**
	 * Get DAO implementation working with name translations
	 *
	 * @return GenericDao implementation
	 */
	protected GenericDao<DistrictNameTranslation, Long> getNameTranslationDao() {
		return districtNameTranslationDao;
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
	 * Setter for property 'parentService'.
	 *
	 * @param parentService Value to set for property 'parentService'.
	 */
	public void setParentService(ParentService<TownFilter> parentService) {
		this.parentService = parentService;
	}

	/**
	 * Getter for property 'newNameTemporal'.
	 *
	 * @return Value for property 'newNameTemporal'.
	 */
	protected DistrictNameTemporal getNewNameTemporal() {
		return new DistrictNameTemporal();
	}

	/**
	 * Getter for property 'newNameTimeDependent'.
	 *
	 * @return Value for property 'newNameTimeDependent'.
	 */
	protected District getNewNameTimeDependent() {
		return new District();
	}

	/**
	 * Getter for property 'emptyName'.
	 *
	 * @return Value for property 'emptyName'.
	 */
	protected DistrictName getEmptyName() {
		return new DistrictName();
	}

	/**
	 * Create empty name translation
	 *
	 * @return name translation
	 */
	public DistrictNameTranslation getEmptyNameTranslation() {
		return new DistrictNameTranslation();
	}

	/**
	 * Check if disable operation on object is allowed
	 *
	 * @param district  Name time dependent object
	 * @param container Exceptions container to add exception for
	 * @return <code>true</code> if operation allowed, or <code>false</otherwise>
	 */
	protected boolean canDisable(District district,
								 FlexPayExceptionContainer container) {
		return true;
	}

	public DistrictFilter initFilter(DistrictFilter parentFilter,
									 PrimaryKeyFilter forefatherFilter, Locale locale)
			throws FlexPayException {
		if (parentFilter == null) {
			parentFilter = new DistrictFilter();
		}

		parentFilter.setNames(getTranslations(forefatherFilter, locale));

		Collection<DistrictNameTranslation> names = parentFilter.getNames();
		if (names.isEmpty()) {
			throw new FlexPayException("No district names", "ab.no_districts");
		}
		if (parentFilter.getSelectedId() == null
			|| !isFilterValid(parentFilter)) {
			DistrictName firstObject = (DistrictName) names.iterator().next()
					.getTranslatable();
			parentFilter.setSelectedId(firstObject.getObject().getId());
		}

		return parentFilter;

	}

	private boolean isFilterValid(DistrictFilter filter) {
		for (DistrictNameTranslation nameTranslation : filter.getNames()) {
			DistrictName name = (DistrictName) nameTranslation
					.getTranslatable();
			if (name.getObject().getId().equals(filter.getSelectedId())) {
				return true;
			}
		}

		return false;
	}

	public ArrayStack initFilters(ArrayStack filters, Locale locale)
			throws FlexPayException {
		if (filters == null) {
			filters = new ArrayStack();
		}

		DistrictFilter parentFilter = filters.isEmpty() ? null
														: (DistrictFilter) filters.pop();
		filters = parentService.initFilters(filters, locale);
		TownFilter forefatherFilter = (TownFilter) filters.peek();

		// init filter
		parentFilter = initFilter(parentFilter, forefatherFilter, locale);
		filters.push(parentFilter);

		return filters;
	}

	/**
	 * return base for name time-dependent objects in i18n files, like 'region', 'town', etc.
	 *
	 * @return Localization key base
	 */
	protected String getI18nKeyBase() {
		return "ab.district";
	}

	/**
	 * Create or update district
	 *
	 * @param object District to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	public void save(@NotNull District object) throws FlexPayExceptionContainer {
		validate(object);
		if (object.isNew()) {
			object.setId(null);
			districtDao.create(object);
		} else {
			districtDao.update(object);
		}
	}

	/**
	 * validate district before save
	 *
	 * @param object District object to validate
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull District object) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		if (object.getParent() == null) {
			ex.addException(new FlexPayException("No town", "error.ab.district.no_town"));
		}

		Collection<DistrictNameTemporal> temporals = object.getNameTemporals();
		if (temporals.isEmpty()) {
			ex.addException(new FlexPayException("No names", "error.ab.district.no_names"));
		}

		boolean first = true;
		for (DistrictNameTemporal temporal : temporals) {

			// the second and all next names should have default lang translation
			if (!first || temporals.size() == 1) {
				DistrictName name = object.getNameForDate(DateUtil.now());
				try {
					if (name == null || StringUtils.isBlank(name.getDefaultNameTranslation())) {
						FlexPayException e = new FlexPayException("No translation", "error.ab.district.no_default_translation",
								temporal.getBegin(), temporal.getEnd());
						ex.addException(e);

						log.debug("Period: {} - {} is empty ", temporal.getBegin(), temporal.getEnd());
					}
				} catch (FlexPayException e) {
					ex.addException(e);
				}
			}

			first = false;
		}

		if (ex.isNotEmpty()) {
			throw ex;
		}
	}
}
