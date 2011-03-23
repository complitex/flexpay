package org.flexpay.bti.service.history;

import org.flexpay.bti.persistence.building.BuildingAttributeType;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;

import static org.flexpay.common.persistence.Stub.stub;

public class BuildingAttributeTypeHistoryGenerator implements HistoryGenerator<BuildingAttributeType> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private BuildingAttributeTypeHistoryBuilder historyBuilder;
	private BuildingAttributeTypeReferencesHistoryGenerator referencesHistoryGenerator;
	private BuildingAttributeTypeService typeService;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull BuildingAttributeType obj) {

		if (diffService.hasDiffs(obj)) {
			log.debug("BuildingAttributeType already has history, do nothing {}", obj.getId());
			return;
		}

		BuildingAttributeType group = typeService.readFull(stub(obj));
		if (group == null) {
			log.warn("Requested type history generation, but not found: {}", obj.getId());
			return;
		}

		referencesHistoryGenerator.generateReferencesHistory(group);

		if (!diffService.hasDiffs(obj)) {
			Diff diff = historyBuilder.diff(null, group);
			diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			diffService.create(diff);
		}
	}

	@Override
	public void generateFor(@NotNull Collection<BuildingAttributeType> objs) {
		for (BuildingAttributeType type : objs) {
			generateFor(type);
		}
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setHistoryBuilder(BuildingAttributeTypeHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setTypeService(BuildingAttributeTypeService typeService) {
		this.typeService = typeService;
	}

	@Required
	public void setReferencesHistoryGenerator(BuildingAttributeTypeReferencesHistoryGenerator referencesHistoryGenerator) {
		this.referencesHistoryGenerator = referencesHistoryGenerator;
	}
}
