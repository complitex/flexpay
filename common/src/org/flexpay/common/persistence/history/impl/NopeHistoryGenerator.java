package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class NopeHistoryGenerator <T extends DomainObject> implements HistoryGenerator<T> {

	/**
	 * Does nothing
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull T obj) {
		// do nothing
	}

	@Override
	public void generateFor(@NotNull Collection<T> objs) {
		// do nothing
	}
}
