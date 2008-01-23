package org.flexpay.ab.service.imp;

import org.flexpay.ab.dao.*;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.NameTimeDependentDao;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.service.imp.NameTimeDependentServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.HashSet;

public class StreetServiceImpl extends NameTimeDependentServiceImpl<
		StreetNameTranslation, StreetName, StreetNameTemporal, Street, Town>
		implements StreetService {

	private StreetDao streetDao;
	private StreetNameDao streetNameDao;
	private StreetNameTemporalDao streetNameTemporalDao;
	private StreetNameTranslationDao streetNameTranslationDao;
	private TownDao townDao;

	private ParentService<TownNameTranslation, TownFilter> parentService;


	/**
	 * Setter for property 'streetDao'.
	 *
	 * @param streetDao Value to set for property 'streetDao'.
	 */
	public void setStreetDao(StreetDao streetDao) {
		this.streetDao = streetDao;
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
	public void setParentService(ParentService<TownNameTranslation, TownFilter> parentService) {
		this.parentService = parentService;
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
	 * return base for name time-dependent objects in i18n files, like 'region', 'town',
	 * etc.
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
}
