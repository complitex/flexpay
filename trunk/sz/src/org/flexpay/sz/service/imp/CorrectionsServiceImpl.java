package org.flexpay.sz.service.imp;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.Street;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.sz.dao.ApartmentCorrectionDao;
import org.flexpay.sz.dao.BuildingCorrectionDao;
import org.flexpay.sz.dao.DistrictCorrectionDao;
import org.flexpay.sz.dao.StreetCorrectionDao;
import org.flexpay.sz.persistence.Oszn;
import org.flexpay.sz.persistence.corrections.ApartmentNumberCorrection;
import org.flexpay.sz.persistence.corrections.BuildingNumberCorrection;
import org.flexpay.sz.persistence.corrections.DistrictCorrection;
import org.flexpay.sz.persistence.corrections.StreetCorrection;
import org.flexpay.sz.service.CorrectionsService;

public class CorrectionsServiceImpl implements CorrectionsService {

	private DistrictCorrectionDao districtCorrectionDao;
	private BuildingCorrectionDao buildingCorrectionDao;
	private StreetCorrectionDao streetCorrectionDao;
	private ApartmentCorrectionDao apartmentCorrectionDao;

	/**
	 * Setter for property 'districtCorrectionDao'.
	 *
	 * @param districtCorrectionDao Value to set for property 'districtCorrectionDao'.
	 */
	public void setDistrictCorrectionDao(DistrictCorrectionDao districtCorrectionDao) {
		this.districtCorrectionDao = districtCorrectionDao;
	}

	/**
	 * Setter for property 'buildingCorrectionDao'.
	 *
	 * @param buildingCorrectionDao Value to set for property 'buildingCorrectionDao'.
	 */
	public void setBuildingCorrectionDao(BuildingCorrectionDao buildingCorrectionDao) {
		this.buildingCorrectionDao = buildingCorrectionDao;
	}

	/**
	 * Setter for property 'streetCorrectionDao'.
	 *
	 * @param streetCorrectionDao Value to set for property 'streetCorrectionDao'.
	 */
	public void setStreetCorrectionDao(StreetCorrectionDao streetCorrectionDao) {
		this.streetCorrectionDao = streetCorrectionDao;
	}

	/**
	 * Setter for property 'apartmentCorrectionDao'.
	 *
	 * @param apartmentCorrectionDao Value to set for property 'apartmentCorrectionDao'.
	 */
	public void setApartmentCorrectionDao(ApartmentCorrectionDao apartmentCorrectionDao) {
		this.apartmentCorrectionDao = apartmentCorrectionDao;
	}

	/**
	 * Find District correction for external organization
	 *
	 * @param oszn		 External organisation
	 * @param externalCode External organisation district code
	 * @return DistrictCorrection
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if correction could not be found
	 */
	public DistrictCorrection findDistrictCorrection(Oszn oszn, String externalCode)
			throws FlexPayException {

		DistrictCorrection correction = districtCorrectionDao
				.findCorrection(oszn.getId(), externalCode);
		if (correction != null) {
			return correction;
		}

		throw new FlexPayException("Cannot find district correction",
				"error.correction.district", oszn.getDescription(), externalCode);
	}

	public StreetCorrection findStreetCorrection(Oszn oszn, String externalCode, District district)
			throws FlexPayException {

		StreetCorrection correction = streetCorrectionDao
				.findCorrection(oszn.getId(), district.getId(), externalCode);
		if (correction != null) {
			return correction;
		}

		throw new FlexPayException("Cannot find street correction",
				"error.correction.street", oszn.getDescription(), externalCode);
	}

	public BuildingNumberCorrection findBuildingCorrection(Oszn oszn, String externalCode, String bulk, Street street)
			throws FlexPayException {

		BuildingNumberCorrection correction = buildingCorrectionDao
				.findCorrection(oszn.getId(), street.getId(), externalCode, bulk);
		if (correction != null) {
			return correction;
		}

		throw new FlexPayException("Cannot find building correction",
				"error.correction.building", oszn.getDescription(), externalCode);
	}

	public ApartmentNumberCorrection findApartmentCorrection(Oszn oszn, String externalCode, Building building)
			throws FlexPayException {

		ApartmentNumberCorrection correction = apartmentCorrectionDao
				.findCorrection(oszn.getId(), building.getId(), externalCode);
		if (correction != null) {
			return correction;
		}

		throw new FlexPayException("Cannot find apartment correction",
				"error.correction.apartment", oszn.getDescription(), externalCode);
	}
}
