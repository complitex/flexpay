package org.flexpay.payments.service.impl.fetch;

import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordProperties;
import org.flexpay.common.service.impl.fetch.ProcessingReadHintsHandler;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.service.DocumentService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.map;

/**
 * Search eirc accounts in documents
 */
public class EircAccountReadHintsHandler extends ProcessingReadHintsHandler {

	private DocumentService documentService; 

	public EircAccountReadHintsHandler(@NotNull List<RegistryRecord> records, @NotNull DocumentService documentService) {
		super(null, null, records);
		this.documentService = documentService;
	}

	@Override
	public void read() {

		List<Long>  operationIds = list();
		for (RegistryRecord record : records) {
			if (record.getPersonalAccountExt() != null && record.getUniqueOperationNumber() != null) {
				operationIds.add(record.getUniqueOperationNumber());
			}
		}
		List<Document> documents = documentService.searchDocuments(operationIds);

		Map<String, Document> docs = map();
		for (Document document : documents) {
			docs.put(document.getCreditorId() + "," + document.getOperation().getId(), document);
		}
		for (RegistryRecord record : records) {
			if (record.getPersonalAccountExt() != null && record.getUniqueOperationNumber() != null) {
				String key = record.getPersonalAccountExt() + "," + record.getUniqueOperationNumber();
				final Document document = docs.get(key);
				if (document != null) {
					record.setProperties(new RegistryRecordProperties() {
						private String eircAccount = document.getDebtorId();

						public String getEircAccount() {
							return eircAccount;
						}
					});
				}
			}
		}

	}

}
