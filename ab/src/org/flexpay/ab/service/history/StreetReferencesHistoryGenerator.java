package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetTypeTemporal;
import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class StreetReferencesHistoryGenerator implements ReferencesHistoryGenerator<Street> {

	private StreetTypeHistoryGenerator streetTypeHistoryGenerator;
	private TownHistoryGenerator townHistoryGenerator;

	@Override
	public void generateReferencesHistory(@NotNull Street obj) {

		townHistoryGenerator.generateFor(obj.getTown());

		for (StreetTypeTemporal temporal : obj.getTypeTemporals()) {
			if (!temporal.isValueEmpty()) {
				streetTypeHistoryGenerator.generateFor(temporal.getValue());
			}
		}
	}

	@Required
	public void setStreetTypeHistoryGenerator(StreetTypeHistoryGenerator streetTypeHistoryGenerator) {
		this.streetTypeHistoryGenerator = streetTypeHistoryGenerator;
	}

	@Required
	public void setTownHistoryGenerator(TownHistoryGenerator townHistoryGenerator) {
		this.townHistoryGenerator = townHistoryGenerator;
	}
}
