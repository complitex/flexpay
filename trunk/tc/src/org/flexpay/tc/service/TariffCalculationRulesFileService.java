package org.flexpay.tc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.Set;
import java.util.Collection;

public interface TariffCalculationRulesFileService {

	@Secured (Roles.TARIFF_CALCULATION_RULES_FILE_ADD)
	void create(@NotNull TariffCalculationRulesFile tariffCalculationRulesFile);

	@Secured (Roles.TARIFF_CALCULATION_RULES_FILE_CHANGE)
	void update(@NotNull TariffCalculationRulesFile tariffCalculationRulesFile);

	@Secured (Roles.TARIFF_CALCULATION_RULES_FILE_READ)
	TariffCalculationRulesFile read(@NotNull Stub<TariffCalculationRulesFile> stub);

	@Secured ({Roles.TARIFF_CALCULATION_RULES_FILE_DELETE})
	void disable(@NotNull Set<Long> objectIds);

	@Secured ({Roles.TARIFF_CALCULATION_RULES_FILE_DELETE})
	void delete(TariffCalculationRulesFile file);

	@Secured ({Roles.TARIFF_CALCULATION_RULES_FILE_DELETE})
	void delete(@NotNull Stub<TariffCalculationRulesFile> fileStub);

	@Secured ({Roles.TARIFF_CALCULATION_RULES_FILE_DELETE})
	void disableByIds(@NotNull Collection<Long> objectIds) throws FlexPayExceptionContainer;

	@Secured ({Roles.TARIFF_CALCULATION_RULES_FILE_READ})
	List<TariffCalculationRulesFile> listTariffCalculationRulesFiles(Page<TariffCalculationRulesFile> pager);

}
