package org.flexpay.common.process.filter;

import org.jetbrains.annotations.NotNull;

/**
 * ProcessInstance name wrapper for using in {@link org.flexpay.common.process.filter.ProcessNameFilter}
 */
public class ProcessNameObject {

	private Long id;
	private String name;

	public ProcessNameObject(@NotNull Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
