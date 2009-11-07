package org.flexpay.ab.persistence.sorter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.common.persistence.sorter.ObjectSorter;

import java.util.Collections;
import java.util.List;

public class BuildingsSorter extends ObjectSorter {

	private String buildingsField;
	private List<AddressAttributeType> types = Collections.emptyList();

	public void setBuildingsField(String buildingsField) {
		this.buildingsField = buildingsField;
	}

	public void setTypes(List<AddressAttributeType> types) {
		this.types = types;
	}

	/**
	 * Add HQL addendum for FROM clause
	 *
	 * @param query HQL query to update
	 */
	@Override
	public void setFrom(StringBuilder query) {
		for (AddressAttributeType type : types) {
			Long id = type.getId();
			query.append("  left join ").append(buildingsField).append(".buildingAttributes bsorta").append(id).
					append(" with (bsorta").append(id).append(".buildingAttributeType.id=").append(id).append(") ");
		}
	}

	/**
	 * Add HQL addendum for ORDER BY clause
	 *
	 * @param orderByClause HQL query to update
	 */
	@Override
	public void setOrderBy(StringBuilder orderByClause) {

		boolean firstOrderBy = true;
		if (orderByClause.length() > 0) {
			orderByClause.append(",");
			firstOrderBy = false;
		}

		for (AddressAttributeType type : types) {
			Long id = type.getId();
			if (!firstOrderBy) {
				orderByClause.append(", ");
			} else {
				firstOrderBy = false;
			}
			orderByClause.append("lpad(convert(ifnull(bsorta").append(id).append(".value, '0'), UNSIGNED), 10, '0') ")
					.append(getOrder());
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("order", getOrder()).
				append("active", getActive()).
				append("buildingsField", buildingsField).
				toString();
	}
}
