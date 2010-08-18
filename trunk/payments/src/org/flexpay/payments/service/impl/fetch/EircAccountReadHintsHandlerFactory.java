package org.flexpay.payments.service.impl.fetch;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.fetch.ReadHints;
import org.flexpay.common.service.fetch.ReadHintsHandler;
import org.flexpay.common.service.impl.fetch.ProcessingReadHintsHandlerFactory;
import org.flexpay.payments.service.DocumentService;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class EircAccountReadHintsHandlerFactory extends ProcessingReadHintsHandlerFactory {
	private DocumentService documentService;

	@Override
	protected ReadHintsHandler doGetInstance(Stub<Registry> registryStub, FetchRange range, List<RegistryRecord> records) {
		return new EircAccountReadHintsHandler(records, documentService);
	}

	@Override
	public boolean supports(ReadHints hints) {
		return hints.hintSet(ReadHintsConstants.READ_EIRC_ACCOUNT);
	}

	@Required
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
}
