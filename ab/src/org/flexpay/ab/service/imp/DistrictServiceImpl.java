package org.flexpay.ab.service.imp;

import org.flexpay.ab.dao.*;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.NameTimeDependentDao;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.service.imp.NameTimeDependentServiceImpl;

public class DistrictServiceImpl extends NameTimeDependentServiceImpl<
		DistrictNameTranslation, DistrictName, DistrictNameTemporal, District, Town>
		implements DistrictService {

	private DistrictDao districtDao;
	private DistrictNameDao districtNameDao;
	private DistrictNameTemporalDao districtNameTemporalDao;
	private DistrictNameTranslationDao districtNameTranslationDao;
	private TownDao townDao;

	private ParentService<TownNameTranslation, TownFilter> parentService;

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
	public void setDistrictNameTemporalDao(DistrictNameTemporalDao districtNameTemporalDao) {
		this.districtNameTemporalDao = districtNameTemporalDao;
	}

	/**
	 * Setter for property 'districtNameTranslationDao'.
	 *
	 * @param districtNameTranslationDao Value to set for property 'districtNameTranslationDao'.
	 */
	public void setDistrictNameTranslationDao(DistrictNameTranslationDao districtNameTranslationDao) {
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
	public void setParentService(ParentService<TownNameTranslation, TownFilter> parentService) {
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
	protected boolean canDisable(District district, FlexPayExceptionContainer container) {
		return true;
	}

	/**
	 * return base for name time-dependent objects in i18n files, like 'region', 'town',
	 * etc.
	 *
	 * @return Localization key base
	 */
	protected String getI18nKeyBase() {
		return "ab.district";
	}
}
