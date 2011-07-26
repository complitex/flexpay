package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.Collection;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

public class ApartmentHistoryGenerator implements HistoryGenerator<Apartment> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;

	private ApartmentService apartmentService;
	private ApartmentHistoryBuilder historyBuilder;
	private ApartmentReferencesHistoryGenerator referencesHistoryGenerator;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	@Override
	public void generateFor(@NotNull Apartment obj) {

		log.debug("starting generating history for apartment #{}", obj.getId());

		Apartment apartment = apartmentService.readFull(stub(obj));
		if (apartment == null) {
			log.warn("Apartment not found {}", obj);
			return;
		}

		generateForSingle(apartment);
	}

	private void generateForSingle(Apartment apartment) {
		referencesHistoryGenerator.generateReferencesHistory(apartment);

		if (!diffService.hasDiffs(apartment)) {
			// create apartment history
			Diff diff = historyBuilder.diff(null, apartment);
			diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			diffService.create(diff);
		}
	}

	@Override
	public void generateFor(@NotNull Collection<Apartment> objs) {
		List<Apartment> apartments = apartmentService.readFull(DomainObject.collectionIds(objs), false);
		for (Apartment apartment : apartments) {
			log.debug("starting generating history for apartment #{}", apartment.getId());
			generateForSingle(apartment);
		}
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        diffService.setJpaTemplate(jpaTemplate);
        historyBuilder.setJpaTemplate(jpaTemplate);
        apartmentService.setJpaTemplate(jpaTemplate);
        referencesHistoryGenerator.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setHistoryBuilder(ApartmentHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	@Required
	public void setReferencesHistoryGenerator(ApartmentReferencesHistoryGenerator referencesHistoryGenerator) {
		this.referencesHistoryGenerator = referencesHistoryGenerator;
	}

}
