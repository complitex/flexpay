package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.*;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.Collection;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

public class BuildingHistoryGenerator implements HistoryGenerator<Building> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private BuildingService buildingService;
	private DiffService diffService;

	private HistoryBuilder<Building> historyBuilder;
	private ReferencesHistoryGenerator<Building> referencesHistoryGenerator;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	@Override
	public void generateFor(@NotNull Building obj) {

		log.debug("starting generating history for building #{}", obj.getId());

		// create building history
		Building building = buildingService.readFull(stub(obj));
		if (building == null) {
			log.warn("Building not found #{}", obj.getId());
			return;
		}

		generateForSingle(building);
	}

	@Override
	public void generateFor(@NotNull Collection<Building> objs) {

		List<Building> buildings = buildingService.readFull(DomainObject.collectionIds(objs), false);
		for (Building building : buildings) {
			log.debug("starting generating history for building #{}", building.getId());
			generateForSingle(building);
		}
	}

	private void generateForSingle(Building building) {
		referencesHistoryGenerator.generateReferencesHistory(building);

		if (!diffService.hasDiffs(building)) {
			Diff diff = historyBuilder.diff(null, building);
			diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			diffService.create(diff);
		}
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        diffService.setJpaTemplate(jpaTemplate);
        buildingService.setJpaTemplate(jpaTemplate);
        historyBuilder.setJpaTemplate(jpaTemplate);
        referencesHistoryGenerator.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setHistoryBuilder(HistoryBuilder<Building> historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setReferencesHistoryGenerator(ReferencesHistoryGenerator<Building> referencesHistoryGenerator) {
		this.referencesHistoryGenerator = referencesHistoryGenerator;
	}

}
