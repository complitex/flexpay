package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.District;
import org.flexpay.ab.service.DistrictService;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DistrictHistoryGenerator implements HistoryGenerator<District> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DistrictService districtService;
	private DiffService diffService;

	private DistrictHistoryBuilder historyBuilder;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull District obj) {

		log.debug("starting generating history for district {}", obj);

		// create district history
		District district = districtService.readFull(stub(obj));
		if (district == null) {
			log.warn("Town not found {}", district);
			return;
		}

		if (diffService.hasDiffs(district)) {
			log.info("District already has history, do nothing {}", district);
			return;
		}

		Diff diff = historyBuilder.diff(null, district);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);

		log.debug("Ended generating history for district {}", obj);
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@Required
	public void setHistoryBuilder(DistrictHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}
}
