package org.flexpay.eirc.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.eirc.persistence.Subdivision;

import java.util.List;

public class SubdivisionFilter extends PrimaryKeyFilter {

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