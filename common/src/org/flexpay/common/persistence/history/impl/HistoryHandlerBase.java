package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.persistence.history.HistoryHandler;
import org.flexpay.common.persistence.history.HistoryBuilder;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.springframework.beans.factory.annotation.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HistoryHandlerBase<T extends DomainObject> implements HistoryHandler {

	protected Logger log = LoggerFactory.getLogger(getClass());

	protected HistoryBuilder<T> historyBuilder;
	protected ClassToTypeRegistry typeRegistry;
	protected MasterIndexService masterIndexService;
	protected CorrectionsService correctionsService;

	protected void saveMasterCorrection(T obj, Diff diff) {
		DataCorrection correction =
				correctionsService.getStub(diff.getMasterIndex(), obj, masterIndexService.getMasterSourceDescription());
		log.debug("Saving master correction: {}", correction);
		correctionsService.save(correction);
	}

	@Required
	public void setHistoryBuilder(HistoryBuilder<T> historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setTypeRegistry(ClassToTypeRegistry typeRegistry) {
		this.typeRegistry = typeRegistry;
	}

	@Required
	public void setMasterIndexService(MasterIndexService masterIndexService) {
		this.masterIndexService = masterIndexService;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}
}
