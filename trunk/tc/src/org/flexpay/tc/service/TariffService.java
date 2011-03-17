package org.flexpay.tc.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.persistence.Tariff;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.Set;

public interface TariffService {

	@Secured (Roles.TARIFF_ADD)
	void create(@NotNull Tariff tariff);

	@Secured (Roles.TARIFF_CHANGE)
	void update(@NotNull Tariff tariff);

	@Secured (Roles.TARIFF_READ)
	List<Tariff> listTariffs();

	@Secured (Roles.TARIFF_READ)
	Tariff readFull(@NotNull Stub<Tariff> stub);

	@Secured ({Roles.TARIFF_DELETE})
	void disable(@NotNull Set<Long> objectIds);

	/**
	 * Find tariff by code
	 *
	 * @param code tariff code
	 * @return tariff
	 */
	@Secured (Roles.TARIFF_READ)
	@Nullable
	Tariff getTariffByCode(@NotNull String code);
}
