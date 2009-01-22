package org.flexpay.tc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.List;
import java.util.Set;

public interface TariffCalculationRulesFileService {

	@Secured({Roles.TARIFF_CALCULATION_RULES_FILE_ADD, Roles.TARIFF_CALCULATION_RULES_FILE_CHANGE})
	void save(@NotNull TariffCalculationRulesFile tariffCalculationRulesFile);

	@Secured(Roles.TARIFF_CALCULATION_RULES_FILE_READ)
	TariffCalculationRulesFile read(@NotNull Stub<TariffCalculationRulesFile> stub);

	@Secured({Roles.TARIFF_CALCULATION_RULES_FILE_DELETE})
	void disable(@NotNull Set<Long> objectIds);

	@Secured({Roles.TARIFF_CALCULATION_RULES_FILE_READ})
	List<TariffCalculationRulesFile> listTariffCalculationRulesFiles(Page<TariffCalculationRulesFile> pager);

}
