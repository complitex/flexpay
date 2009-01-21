package org.flexpay.tc.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.persistence.Tariff;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.Set;

public interface TariffService {

	@Secured({Roles.TARIFF_ADD, Roles.TARIFF_CHANGE})
	void save(@NotNull Tariff tariff);

	@Secured(Roles.TARIFF_READ)
	Tariff getTariff(@NotNull Stub<Tariff> stub);

	@Secured({Roles.TARIFF_DELETE})
	void disable(@NotNull Set<Long> objectIds);

}
