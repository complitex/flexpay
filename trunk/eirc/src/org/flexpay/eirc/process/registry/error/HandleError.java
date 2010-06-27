package org.flexpay.eirc.process.registry.error;

import org.flexpay.common.persistence.ImportError;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;

public interface HandleError {
	ImportError handleError(Throwable t, ProcessingContext context) throws Exception;
}
