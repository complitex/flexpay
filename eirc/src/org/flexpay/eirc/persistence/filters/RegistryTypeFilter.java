package org.flexpay.eirc.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.eirc.persistence.SpRegistryType;

import java.util.List;

public class RegistryTypeFilter extends PrimaryKeyFilter {

	private List<SpRegistryType> registryTypes;

	public RegistryTypeFilter() {
		super(-1L);
	}

	public List<SpRegistryType> getRegistryTypes() {
		return registryTypes;
	}

	public void setRegistryTypes(List<SpRegistryType> registryTypes) {
		this.registryTypes = registryTypes;
	}
}