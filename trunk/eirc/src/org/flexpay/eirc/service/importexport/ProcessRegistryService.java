package org.flexpay.eirc.service.importexport;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.flexpay.eirc.persistence.registry.ProcessRegistryVariableInstance;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ProcessRegistryService {
	public ProcessingContext prepareContext(@NotNull Registry registry) throws FlexPayException;

	public boolean processRegistryRecordRange(@NotNull FetchRange range, @NotNull ProcessRegistryVariableInstance variable, @NotNull ProcessingContext context);

	public boolean processRegistryRecordEnumeration(@NotNull List<Long> recordIds, @NotNull ProcessRegistryVariableInstance variable, @NotNull ProcessingContext context);

	public ProcessRegistryVariableInstance prepare(@NotNull ProcessingContext context, @NotNull ProcessRegistryVariableInstance variable) throws Exception;

	public boolean endProcessing(@NotNull ProcessingContext context);

	public void printWatch();
}
