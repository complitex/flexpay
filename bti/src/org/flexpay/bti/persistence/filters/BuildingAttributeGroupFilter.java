package org.flexpay.bti.persistence.filters;

import org.flexpay.bti.persistence.building.BuildingAttributeGroup;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;

import java.util.Collections;
import java.util.List;

public class BuildingAttributeGroupFilter extends PrimaryKeyFilter<BuildingAttributeGroup> {

	private List<BuildingAttributeGroup> groups = Collections.emptyList();

	public List<BuildingAttributeGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<BuildingAttributeGroup> groups) {
		this.groups = groups;
	}
}
