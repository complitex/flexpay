package org.flexpay.orgs.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.orgs.persistence.Subdivision;

import java.util.List;

public class SubdivisionFilter extends PrimaryKeyFilter<Subdivision> {

	private List<Subdivision> subdivisions;

	public SubdivisionFilter() {
		super(-1L);
	}

	public List<Subdivision> getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(List<Subdivision> subdivisions) {
		this.subdivisions = subdivisions;
	}

	public Subdivision getSelected() {
		for (Subdivision subdivision : subdivisions) {
			if (subdivision.getId().equals(getSelectedId())) {
				return subdivision;
			}
		}

		return null;
	}

}
