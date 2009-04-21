package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.ProcessingStatus;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.DiffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class ApartmentHistoryGenerator implements HistoryGenerator<Apartment> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private ApartmentService apartmentService;
	private DiffService diffService;

	private ApartmentHistoryBuilder historyBuilder;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull Apartment obj) {

		log.debug("starting generating history for apartment {}", obj);

		// create apartment history
		Apartment apartment = apartmentService.readWithPersons(stub(obj));
		if (apartment == null) {
			log.warn("Apartment not found {}", apartment);
			return;
		}

		if (diffService.hasDiffs(apartment)) {
			log.info("Apartment already has history, do nothing {}", apartment);
			return;
		}

		Diff diff = historyBuilder.diff(null, apartment);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);

		log.debug("Ended generating history for apartment {}", obj);
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setHistoryBuilder(ApartmentHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}
}
