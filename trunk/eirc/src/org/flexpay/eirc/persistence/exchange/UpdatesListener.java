package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;

/**
 * Marker interface for delayed updates that need to perform additional actions before update is done
 */
public interface UpdatesListener {

	void nextRecord(ProcessingContext context);

	void beforeUpdate(ProcessingContext context) throws FlexPayException;
}
