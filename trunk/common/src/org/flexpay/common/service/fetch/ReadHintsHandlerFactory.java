package org.flexpay.common.service.fetch;

public interface ReadHintsHandlerFactory {

	boolean supports(ReadHints hints);

	ReadHintsHandler getInstance(Object... params);
}
