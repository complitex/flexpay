package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.service.AddressAttributeTypeService;
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

public class AddressAttributeTypeHistoryGenerator implements HistoryGenerator<AddressAttributeType> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private AddressAttributeTypeService typeService;
	private DiffService diffService;

	private AddressAttributeTypeHistoryBuilder historyBuilder;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	@Override
	public void generateFor(@NotNull AddressAttributeType obj) {

		if (diffService.hasDiffs(obj)) {
			log.info("Address attribute type already has history, do nothing #{}", obj.getId());
			return;
		}

		AddressAttributeType type = typeService.readFull(stub(obj));
		if (type == null) {
			log.warn("Address attribute type not found {}", obj);
			return;
		}

		Diff diff = historyBuilder.diff(null, type);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);
	}

	@Override
	public void generateFor(@NotNull Collection<AddressAttributeType> objs) {
		for (AddressAttributeType type : objs) {
			generateFor(type);
		}
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setTypeService(AddressAttributeTypeService typeService) {
		this.typeService = typeService;
	}

	@Required
	public void setHistoryBuilder(AddressAttributeTypeHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}
}
