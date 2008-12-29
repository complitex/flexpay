package org.flexpay.eirc.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.eirc.persistence.RegistryType;

import java.util.List;

public class RegistryTypeFilter extends PrimaryKeyFilter {

	private List<RegistryType> registryTypes;

	public RegistryTypeFilter() {
		super(-1L);
	}

	public List<RegistryType> getRegistryTypes() {
		return registryTypes;
	}

	public void setRegistryTypes(List<RegistryType> registryTypes) {
		this.registryTypes = registryTypes;
	}
}