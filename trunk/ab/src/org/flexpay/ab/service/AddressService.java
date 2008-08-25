package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public interface AddressService {

	/**
	 * Find Apartment address
	 *
	 * @param stub Apartment stub
	 * @param locale Locale to get address in
	 * @return Apartment address
	 * @throws Exception if failure occurs
	 */
	@NotNull
	String getAddress(@NotNull Stub<Apartment> stub, @Nullable Locale locale) throws Exception;
}
