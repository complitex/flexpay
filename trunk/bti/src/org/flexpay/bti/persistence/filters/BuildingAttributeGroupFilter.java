package org.flexpay.bti.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.bti.persistence.BuildingAttributeGroup;

import java.util.List;
import java.util.Collections;

public class BuildingAttributeGroupFilter extends PrimaryKeyFilter<BuildingAttributeGroup> {

	private List<BuildingAttributeGroup> groups = Collections.emptyList();

	public List<BuildingAttributeGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<BuildingAttributeGroup> groups) {
		this.groups = groups;
	}
}
