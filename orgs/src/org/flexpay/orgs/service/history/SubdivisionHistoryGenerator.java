package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.flexpay.orgs.persistence.Subdivision;
import org.flexpay.orgs.service.SubdivisionService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

public class SubdivisionHistoryGenerator implements HistoryGenerator<Subdivision> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private SubdivisionHistoryBuilder historyBuilder;
	private SubdivisionService subdivisionService;
	private SubdivisionReferencesHistoryGenerator referencesHistoryGenerator;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull Subdivision obj) {

		Subdivision subdivision = subdivisionService.read(stub(obj));
		if (subdivision == null) {
			log.warn("Requested subdivision history generation, but not found: {}", obj);
			return;
		}

		generateForSingle(subdivision);
	}

	private void generateForSingle(Subdivision subdivision) {
		referencesHistoryGenerator.generateReferencesHistory(subdivision);

		if (!diffService.hasDiffs(subdivision)) {
			Diff diff = historyBuilder.diff(null, subdivision);
			diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			diffService.create(diff);
		}
	}

	/**
	 * Do generation of history for several objects
	 *
	 * @param objs Objects to generate history for
	 */
	@Override
	public void generateFor(@NotNull Collection<Subdivision> objs) {
		List<Subdivision> subdivisions = subdivisionService.readFull(DomainObject.collectionIds(objs), false);
		for (Subdivision subdivision : subdivisions) {
			generateForSingle(subdivision);
		}
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setSubdivisionService(SubdivisionService subdivisionService) {
		this.subdivisionService = subdivisionService;
	}

	@Required
	public void setHistoryBuilder(SubdivisionHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setReferencesHistoryGenerator(SubdivisionReferencesHistoryGenerator referencesHistoryGenerator) {
		this.referencesHistoryGenerator = referencesHistoryGenerator;
	}
}
