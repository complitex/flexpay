package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public interface AddressService {

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
}
