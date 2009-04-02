package org.flexpay.orgs.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.orgs.persistence.OrganizationInstance;
import org.flexpay.orgs.persistence.OrganizationInstanceDescription;

import java.util.Collections;
import java.util.List;

public abstract class OrganizationInstanceFilter
		<D extends OrganizationInstanceDescription, T extends OrganizationInstance<D, T>>
		extends PrimaryKeyFilter<T> {

	private List<T> instances = Collections.emptyList();

	protected OrganizationInstanceFilter() {
	}

	protected OrganizationInstanceFilter(Long selectedId) {
		super(selectedId);
	}

	public List<T> getInstances() {
		return instances;
	}

	public void setInstances(List<T> instances) {
		this.instances = instances;
	}
}
