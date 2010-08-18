package org.flexpay.payments.service.impl.fetch;

import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordProperties;
import org.flexpay.common.service.impl.fetch.ProcessingReadHintsHandler;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.service.DocumentService;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
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
		Iterator<Document> iterDocuments = documentService.searchDocuments(operationIds).iterator();

		Document document = null;
		for (RegistryRecord record : records) {
			if (record.getPersonalAccountExt() != null && record.getUniqueOperationNumber() != null) {
				if (document == null) {
					document = iterDocuments.next();
				}
				if (document != null && record.getUniqueOperationNumber().equals(document.getId())) {
					final String innerEircAccount = document.getDebtorId();
					record.setProperties(new RegistryRecordProperties() {
						private String eircAccount = innerEircAccount;

						public String getEircAccount() {
							return eircAccount;
						}
					});
					document = null;
				}
			}
		}

	}

}
