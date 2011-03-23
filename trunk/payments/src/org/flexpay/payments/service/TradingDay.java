package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DomainObject;
import org.jetbrains.annotations.NotNull;

public interface TradingDay<T extends DomainObject> {

	void startTradingDay(@NotNull T o) throws FlexPayException;

	void stopTradingDay(@NotNull T o) throws FlexPayException;

	boolean isOpened(@NotNull final Long processInstanceId);
}
