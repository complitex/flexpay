package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TownTypeTemporal;
import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

public class TownReferencesHistoryGenerator implements ReferencesHistoryGenerator<Town> {

	private TownTypeHistoryGenerator townTypeHistoryGenerator;
	private RegionHistoryGenerator regionHistoryGenerator;

	@Override
	public void generateReferencesHistory(@NotNull Town obj) {
		for (TownTypeTemporal temporal : obj.getTypeTemporals()) {
			if (!temporal.isValueEmpty()) {
				townTypeHistoryGenerator.generateFor(temporal.getValue());
			}
		}

		regionHistoryGenerator.generateFor(obj.getRegion());
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        townTypeHistoryGenerator.setJpaTemplate(jpaTemplate);
        regionHistoryGenerator.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setTownTypeHistoryGenerator(TownTypeHistoryGenerator townTypeHistoryGenerator) {
		this.townTypeHistoryGenerator = townTypeHistoryGenerator;
	}

	@Required
	public void setRegionHistoryGenerator(RegionHistoryGenerator regionHistoryGenerator) {
		this.regionHistoryGenerator = regionHistoryGenerator;
	}

}
