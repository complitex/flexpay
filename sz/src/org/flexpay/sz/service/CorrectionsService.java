package org.flexpay.sz.service;

import org.flexpay.sz.persistence.corrections.DistrictCorrection;
import org.flexpay.sz.persistence.corrections.StreetCorrection;
import org.flexpay.sz.persistence.corrections.BuildingNumberCorrection;
import org.flexpay.sz.persistence.corrections.ApartmentNumberCorrection;
import org.flexpay.sz.persistence.Oszn;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.Building;

/**
 * Service working with corrections data
 */
public interface CorrectionsService {

	/**
	 * Find District correction for external organization
	 *
	 * @param oszn External organisation
	 * @param externalCode External organisation district code
	 * @return DistrictCorrection
	 * @throws FlexPayException if correction could not be found
	 */
	DistrictCorrection findDistrictCorrection(Oszn oszn, String externalCode)
		throws FlexPayException;

	/**
	 * Find Street correction for external organization
	 *
	 * @param oszn External organisation
	 * @param externalCode External organisation street code
	 * @param district District to find street in
	 * @return StreetCorrection
	 * @throws FlexPayException if correction could not be found
	 */
	StreetCorrection findStreetCorrection(Oszn oszn, String externalCode, District district)
		throws FlexPayException;

	/**
	 * Find Building correction for external organization
	 *
	 * @param oszn External organisation
	 * @param externalCode External organisation building number
	 * @param bulk Optional external organisation building bulk
	 * @param street Street to find building in
	 * @return BuildingNumberCorrection
	 * @throws FlexPayException if correction could not be found
	 */
	BuildingNumberCorrection findBuildingCorrection(Oszn oszn, String externalCode, String bulk, Street street)
		throws FlexPayException;

	/**
	 * Find Apartment correction for external organization
	 *
	 * @param oszn External organisation
	 * @param externalCode External organisation apartment number
	 * @param building Building to find apartment in
	 * @return ApartmentNumberCorrection
	 * @throws FlexPayException if correction could not be found
	 */
	ApartmentNumberCorrection findApartmentCorrection(Oszn oszn, String externalCode, Building building)
		throws FlexPayException;
}
