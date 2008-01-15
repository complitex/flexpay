package org.flexpay.ab.service.imp;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.TownService;
import org.flexpay.ab.dao.*;
import org.flexpay.common.service.imp.NameTimeDependentServiceImpl;
import org.flexpay.common.dao.NameTimeDependentDao;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.exception.FlexPayExceptionContainer;

public class TownServiceImpl extends NameTimeDependentServiceImpl<
		TownNameTranslation, TownName, TownNameTemporal, Town, Region>
		implements TownService {

	private TownDao townDao;
	private TownNameDao townNameDao;
	private TownNameTemporalDao townNameTemporalDao;
	private TownNameTranslationDao townNameTranslationDao;
	private RegionDao regionDao;


	/**
	 * Setter for property 'townDao'.
	 *
	 * @param townDao Value to set for property 'townDao'.
	 */
	public void setTownDao(TownDao townDao) {
		this.townDao = townDao;
	}

	/**
	 * Setter for property 'townNameDao'.
	 *
	 * @param townNameDao Value to set for property 'townNameDao'.
	 */
	public void setTownNameDao(TownNameDao townNameDao) {
		this.townNameDao = townNameDao;
	}

	/**
	 * Setter for property 'townNameTemporalDao'.
	 *
	 * @param townNameTemporalDao Value to set for property 'townNameTemporalDao'.
	 */
	public void setTownNameTemporalDao(TownNameTemporalDao townNameTemporalDao) {
		this.townNameTemporalDao = townNameTemporalDao;
	}

	/**
	 * Setter for property 'townNameTranslationDao'.
	 *
	 * @param townNameTranslationDao Value to set for property 'townNameTranslationDao'.
	 */
	public void setTownNameTranslationDao(TownNameTranslationDao townNameTranslationDao) {
		this.townNameTranslationDao = townNameTranslationDao;
	}

	/**
	 * Setter for property 'regionDao'.
	 *
	 * @param regionDao Value to set for property 'regionDao'.
	 */
	public void setRegionDao(RegionDao regionDao) {
		this.regionDao = regionDao;
	}

	/**
	 * return base for name time-dependent objects in i18n files, like 'region', 'town',
	 * etc.
	 *
	 * @return Localization key base
	 */
	protected String getI18nKeyBase() {
		return "town";
	}

	/**
	 * Get DAO implementation working with Name time-dependent objects
	 *
	 * @return GenericDao implementation
	 */
	protected NameTimeDependentDao<Town, Long> getNameTimeDependentDao() {
		return townDao;
	}

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	protected GenericDao<TownNameTemporal, Long> getNameTemporalDao() {
		return townNameTemporalDao;
	}

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	protected GenericDao<TownName, Long> getNameValueDao() {
		return townNameDao;
	}

	/**
	 * Get DAO implementation working with name translations
	 *
	 * @return GenericDao implementation
	 */
	protected GenericDao<TownNameTranslation, Long> getNameTranslationDao() {
		return townNameTranslationDao;
	}

	/**
	 * Get DAO implementation working with parent objects
	 *
	 * @return GenericDao implementation
	 */
	protected GenericDao<Region, Long> getParentDao() {
		return regionDao;
	}

	/**
	 * Check if disable operation on object is allowed
	 *
	 * @param town	   Name time dependent object
	 * @param container Exceptions container to add exception for
	 * @return <code>true</code> if operation allowed, or <code>false</otherwise>
	 */
	protected boolean canDisable(Town town, FlexPayExceptionContainer container) {
		return true;
	}

	/**
	 * Getter for property 'newNameTemporal'.
	 *
	 * @return Value for property 'newNameTemporal'.
	 */
	protected TownNameTemporal getNewNameTemporal() {
		return new TownNameTemporal();
	}

	/**
	 * Getter for property 'newNameTimeDependent'.
	 *
	 * @return Value for property 'newNameTimeDependent'.
	 */
	protected Town getNewNameTimeDependent() {
		return new Town();
	}

	/**
	 * Getter for property 'emptyName'.
	 *
	 * @return Value for property 'emptyName'.
	 */
	protected TownName getEmptyName() {
		return new TownName();
	}

	/**
	 * Create empty name translation
	 *
	 * @return name translation
	 */
	public TownNameTranslation getEmptyNameTranslation() {
		return new TownNameTranslation();
	}
}
