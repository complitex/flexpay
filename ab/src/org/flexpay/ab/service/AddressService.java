package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.JpaSetService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public interface AddressService extends JpaSetService {

	/**
	 * Get Apartment address
	 *
	 * @param stub Apartment stub
	 * @param locale Locale to get address in
	 * @return Apartment address
	 * @throws Exception if failure occurs
	 */
	@NotNull
	String getAddress(@NotNull Stub<Apartment> stub, @Nullable Locale locale) throws Exception;

	/**
	 * Get Building address
	 *
	 * @param stub Building stub
	 * @param locale Locale to get address in
	 * @return Building address
	 * @throws Exception if failure occurs
	 */
	@NotNull
	String getBuildingAddress(@NotNull Stub<Building> stub, @Nullable Locale locale) throws Exception;

	/**
	 * Get Buildings address
	 *
	 * @param stub Buildings stub
	 * @param locale Locale to get address in
	 * @return Buildings address
	 * @throws Exception if failure occurs
	 */
	@NotNull
	String getBuildingsAddress(@NotNull Stub<BuildingAddress> stub, @Nullable Locale locale) throws Exception;

	/**
	 * Get Building address on street
	 *
	 * @param stub Building stub
	 * @param streetStub Street stub to get address on
	 * @param locale Locale to get address in
	 * @return Building address
	 * @throws Exception if failure occurs
	 */
	@NotNull
	String getBuildingAddressOnStreet(@NotNull Stub<Building> stub, @NotNull Stub<Street> streetStub,  @Nullable Locale locale)
			throws Exception;

}
