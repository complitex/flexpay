package org.flexpay.common.process.filter;

import org.flexpay.common.persistence.DomainObject;
import org.jetbrains.annotations.NotNull;

/**
 * Process name wrapper for using in {@link org.flexpay.common.process.filter.ProcessNameFilter}
 */
public class ProcessNameObject extends DomainObject {

	private String name;

	public ProcessNameObject(@NotNull Long id, String name) {
		super(id);
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
