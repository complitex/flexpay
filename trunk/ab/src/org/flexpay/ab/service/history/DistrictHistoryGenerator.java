package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.District;
import org.flexpay.ab.service.DistrictService;
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

public class DistrictHistoryGenerator implements HistoryGenerator<District> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DistrictService districtService;
	private DiffService diffService;

	private DistrictHistoryBuilder historyBuilder;
	private DistrictReferencesHistoryGenerator referencesHistoryGenerator;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	@Override
	public void generateFor(@NotNull District obj) {

		log.debug("starting generating history for district #{}", obj.getId());

		// create district history
		District district = districtService.readFull(stub(obj));
		if (district == null) {
			log.warn("District not found {}", district);
			return;
		}

		generateForSingle(district);
	}

	@Override
	public void generateFor(@NotNull Collection<District> objs) {

		List<District> districts = districtService.readFull(DomainObject.collectionIds(objs), false);
		for (District district : districts) {
			log.debug("starting generating history for district #{}", district.getId());
			generateForSingle(district);
		}
	}

	private void generateForSingle(District district) {

		referencesHistoryGenerator.generateReferencesHistory(district);

		if (diffService.hasDiffs(district)) {
			log.debug("District already has history, do nothing {}", district);
			return;
		}

		Diff diff = historyBuilder.diff(null, district);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);

		log.debug("Ended generating history for district {}", district);
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        diffService.setJpaTemplate(jpaTemplate);
        districtService.setJpaTemplate(jpaTemplate);
        historyBuilder.setJpaTemplate(jpaTemplate);
        referencesHistoryGenerator.setJpaTemplate(jpaTemplate);
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

	@Required
	public void setReferencesHistoryGenerator(DistrictReferencesHistoryGenerator referencesHistoryGenerator) {
		this.referencesHistoryGenerator = referencesHistoryGenerator;
	}

}
