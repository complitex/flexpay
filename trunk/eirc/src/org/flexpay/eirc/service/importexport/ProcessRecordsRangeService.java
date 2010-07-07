package org.flexpay.eirc.service.importexport;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;

import java.util.Collection;

public interface ProcessRecordsRangeService {

	public ProcessingContext prepareContext(Registry registry) throws FlexPayException;

	public boolean processRecords(Collection<RegistryRecord> records, ProcessingContext context);

	public boolean doUpdate(ProcessingContext context);
}
