package org.flexpay.eirc.process.registry.error;

import org.flexpay.eirc.persistence.exchange.ProcessingContext;

public interface HandleError {
	void handleError(Throwable t, ProcessingContext context) throws Exception;
}
