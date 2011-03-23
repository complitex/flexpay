package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

public class StreetHistoryGenerator implements HistoryGenerator<Street> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private StreetService streetService;
	private DiffService diffService;

	private StreetHistoryBuilder historyBuilder;
	private StreetReferencesHistoryGenerator referencesHistoryGenerator;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	@Override
	public void generateFor(@NotNull Street obj) {

		log.debug("starting generating history for street #{}", obj.getId());

		// create street history
		Street street = streetService.readFull(stub(obj));
		if (street == null) {
			log.warn("Street not found {}", obj);
			return;
		}

		generateForSingle(street);
	}

	@Override
	public void generateFor(@NotNull Collection<Street> objs) {

		List<Street> streets = streetService.readFull(DomainObject.collectionIds(objs), false);
		for (Street street : streets) {
			log.debug("starting generating history for street #{}", street.getId());
			generateForSingle(street);
		}
	}

	private void generateForSingle(Street street) {
		referencesHistoryGenerator.generateReferencesHistory(street);

		if (!diffService.hasDiffs(street)) {
			Diff diff = historyBuilder.diff(null, street);
			diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			diffService.create(diff);
		} else {
			log.debug("Street already has history, do nothing {}", street);
		}
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	@Required
	public void setHistoryBuilder(StreetHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setReferencesHistoryGenerator(StreetReferencesHistoryGenerator referencesHistoryGenerator) {
		this.referencesHistoryGenerator = referencesHistoryGenerator;
	}
}
