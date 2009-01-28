package org.flexpay.bti.persistence;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateIntervalUtil;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;

/**
 * Time dependent building attribute
 */
public class BuildingTempAttribute extends BuildingAttributeBase {

	private Set<BuildingTempAttributeValue> values = Collections.emptySet();

	public Set<BuildingTempAttributeValue> getValues() {
		return values;
	}

	@SuppressWarnings ({"UnusedDeclaration"})
	private void setValues(Set<BuildingTempAttributeValue> values) {
		this.values = values;
	}

	public void addValue(BuildingTempAttributeValue value) {
		value.setAttribute(this);

		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (values == Collections.EMPTY_SET) {
			values = CollectionUtils.set();
		}
		values.add(value);
	}

	public String getValueForDate(Date date) {
		for (BuildingTempAttributeValue value : getValues()) {
			if (DateIntervalUtil.includes(date, value.getBegin(), value.getEnd())) {
				return value.getValue();
			}
		}

		return null;
	}

	public void setValueForDates(String value, Date beginDt, Date endDt) {

		SortedSet<BuildingTempAttributeValue> sortedValues = CollectionUtils.treeSet(getValues());
		Set<BuildingTempAttributeValue> toDelete = CollectionUtils.set();
		for (BuildingTempAttributeValue vl : sortedValues) {
			if (beginDt.after(endDt)) {
				break;
			}
			// check if intervals intersect and setup values
			if (DateIntervalUtil.areIntersecting(vl.getBegin(), vl.getEnd(), beginDt, endDt)) {
				// check if old interval starts before a new one
				if (vl.getBegin().before(beginDt)) {
					addValue(new BuildingTempAttributeValue(vl.getValue(), vl.getBegin(), beginDt));
				}
				if (beginDt.before(vl.getBegin())) {
					addValue(new BuildingTempAttributeValue(value, beginDt, vl.getBegin()));
					beginDt = vl.getBegin();
				}
				if (vl.getEnd().before(endDt)) {
					addValue(new BuildingTempAttributeValue(value, beginDt, vl.getEnd()));
					beginDt = vl.getEnd();
				}

				// mark this vl to be deleted
				toDelete.add(vl);
			}
		}

		// check if there is still interval to add
		if (beginDt.compareTo(endDt) <= 0) {
			addValue(new BuildingTempAttributeValue(value, beginDt, endDt));
		}

		// delete old intersected intervals
		values.removeAll(toDelete);
	}
}
