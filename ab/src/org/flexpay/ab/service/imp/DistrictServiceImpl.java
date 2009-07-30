package org.flexpay.ab.service.imp;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.*;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.DistrictFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.NameTimeDependentDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.persistence.history.ModificationListener;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.common.service.imp.NameTimeDependentServiceImpl;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;

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

	private SessionUtils sessionUtils;
	private ModificationListener<District> modificationListener;

	protected NameTimeDependentDao<District, Long> getNameTimeDependentDao() {
		return districtDao;
	}

	protected GenericDao<DistrictNameTemporal, Long> getNameTemporalDao() {
		return districtNameTemporalDao;
	}

	protected GenericDao<DistrictName, Long> getNameValueDao() {
		return districtNameDao;
	}

	protected GenericDao<DistrictNameTranslation, Long> getNameTranslationDao() {
		return districtNameTranslationDao;
	}

	protected GenericDao<Town, Long> getParentDao() {
		return townDao;
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
	 * Create District object
	 *
	 * @param district District object to save
	 * @return saved object back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	public District create(@NotNull District district) throws FlexPayExceptionContainer {

		validate(district);
		district.setId(null);
		districtDao.create(district);

		modificationListener.onCreate(district);

		return district;
	}

	/**
	 * Create or update Town object
	 *
	 * @param district District object to save
	 * @return saved object back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	public District update(@NotNull District district) throws FlexPayExceptionContainer {

		validate(district);

		District old = readFull(stub(district));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No object found to update " + district));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, district);

		districtDao.update(district);
		return district;
	}

	/**
	 * Disable NTD
	 *
	 * @param objects NTDs to disable
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if failure occurs
	 */
	@Transactional (readOnly = false)
	@Override
	public void disable(Collection<District> objects) throws FlexPayExceptionContainer {

		log.info("{} districts to disable", objects.size());
		for (District object : objects) {
			object.setStatus(District.STATUS_DISABLED);
			districtDao.update(object);

			modificationListener.onDelete(object);

			log.info("Disabled: {}", object);
		}
	}

	/**
	 * Disable districts
	 *
	 * @param objectIds Districts identifiers
	 */
	@Transactional (readOnly = false)
	@Override
	public void disableByIds(@NotNull Collection<Long> objectIds) {
		for (Long id : objectIds) {
			District district = districtDao.read(id);
			if (district != null) {
				district.disable();
				districtDao.update(district);

				modificationListener.onDelete(district);
				log.debug("Disabled: {}", district);
			}
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
				if (name == null || StringUtils.isBlank(name.getDefaultNameTranslation())) {

					if (ApplicationConfig.getPastInfinite().equals(temporal.getBegin())
						&& ApplicationConfig.getFutureInfinite().equals(temporal.getEnd())) {

						FlexPayException e = new FlexPayException("No translation", "error.ab.district.no_default_translation",
								temporal.getBegin(), temporal.getEnd());
						ex.addException(e);

					} else {
						FlexPayException e = new FlexPayException("No translation", "error.ab.district.no_default_translation_for_period",
								temporal.getBegin(), temporal.getEnd());
						ex.addException(e);

						log.debug("Period: {} - {} is empty ", temporal.getBegin(), temporal.getEnd());
					}
				}
			}

			first = false;
		}

		if (ex.isNotEmpty()) {
			ex.info(log);
			throw ex;
		}
	}

	@NotNull
	@Override
	public List<District> findByTownAndQuery(@NotNull Stub<Town> stub, @NotNull String query) {
		return districtDao.findByTownAndQuery(stub.getId(), query);
	}

	@Required
	public void setDistrictDao(DistrictDao districtDao) {
		this.districtDao = districtDao;
	}

	@Required
	public void setDistrictNameDao(DistrictNameDao districtNameDao) {
		this.districtNameDao = districtNameDao;
	}

	@Required
	public void setDistrictNameTemporalDao(DistrictNameTemporalDao districtNameTemporalDao) {
		this.districtNameTemporalDao = districtNameTemporalDao;
	}

	@Required
	public void setDistrictNameTranslationDao(DistrictNameTranslationDao districtNameTranslationDao) {
		this.districtNameTranslationDao = districtNameTranslationDao;
	}

	@Required
	public void setTownDao(TownDao townDao) {
		this.townDao = townDao;
	}

	@Required
	public void setParentService(ParentService<TownFilter> parentService) {
		this.parentService = parentService;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<District> modificationListener) {
		this.modificationListener = modificationListener;
	}

}
