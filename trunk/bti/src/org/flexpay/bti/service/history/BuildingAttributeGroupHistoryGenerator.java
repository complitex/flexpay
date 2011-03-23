package org.flexpay.bti.service.history;

import org.flexpay.bti.persistence.building.BuildingAttributeGroup;
import org.flexpay.bti.service.BuildingAttributeGroupService;
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

public class BuildingAttributeGroupHistoryGenerator implements HistoryGenerator<BuildingAttributeGroup> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private BuildingAttributeGroupHistoryBuilder historyBuilder;
	private BuildingAttributeGroupService groupService;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull BuildingAttributeGroup obj) {

		if (diffService.hasDiffs(obj)) {
			log.debug("BuildingAttributeGroup already has history, do nothing {}", obj.getId());
			return;
		}

		BuildingAttributeGroup group = groupService.readFull(stub(obj));
		if (group == null) {
			log.warn("Requested group history generation, but not found: {}", obj.getId());
			return;
		}

		Diff diff = historyBuilder.diff(null, group);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);
	}

	@Override
	public void generateFor(@NotNull Collection<BuildingAttributeGroup> objs) {
		for (BuildingAttributeGroup group : objs) {
			generateFor(group);
		}
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setHistoryBuilder(BuildingAttributeGroupHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setGroupService(BuildingAttributeGroupService groupService) {
		this.groupService = groupService;
	}
}
