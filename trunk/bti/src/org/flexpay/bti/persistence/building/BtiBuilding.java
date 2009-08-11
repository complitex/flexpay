package org.flexpay.bti.persistence.building;

import org.flexpay.ab.persistence.Building;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BtiBuilding extends Building {

	private Set<BuildingAttribute> attributes = CollectionUtils.set();

	protected BtiBuilding() {
	}

	public BtiBuilding(@NotNull Long id) {
		super(id);
	}

	public BtiBuilding(@NotNull Stub<? extends Building> stub) {
		super(stub.getId());
	}

	public static BtiBuilding newInstance() {
		return new BtiBuilding();
	}

	public Set<BuildingAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<BuildingAttribute> attributes) {
		this.attributes = attributes;
	}

	private Map<BuildingAttributeType, SortedSet<BuildingAttribute>> splitCache = null;

	private Map<BuildingAttributeType, SortedSet<BuildingAttribute>> splitAttributes() {

		if (splitCache != null) {
			return splitCache;
		}

		Map<BuildingAttributeType, SortedSet<BuildingAttribute>> result = CollectionUtils.map();
		for (BuildingAttribute attribute : getAttributes()) {
			BuildingAttributeType type = attribute.getAttributeType();
			SortedSet<BuildingAttribute> group = result.get(type);
			if (group == null) {
				group = CollectionUtils.treeSet();
				result.put(type, group);
			}
			group.add(attribute);
		}

		splitCache = result;

		return result;
	}

	@Nullable
	public BuildingAttribute getCurrentAttribute(BuildingAttributeType attributeType) {
		return getAttributeForDate(attributeType, DateUtil.now());
	}

	@Nullable
	public BuildingAttribute getAttributeForDate(BuildingAttributeType attributeType, Date date) {

		SortedSet<BuildingAttribute> attrs = attributesOfType(attributeType);
		for (BuildingAttribute attribute : attrs) {
			if (DateIntervalUtil.includes(date, attribute.getBegin(), attribute.getEnd())) {
				return attribute;
			}
		}

		return null;
	}

	/**
	 * Set normal attribute
	 *
	 * @param attribute Attribute
	 */
	public void setNormalAttribute(@NotNull BuildingAttribute attribute) {
		if (attribute.notEmpty()) {
			attribute.setTemporal(0);
		}
		doSetAttributeForDates(attribute, ApplicationConfig.getPastInfinite(), ApplicationConfig.getFutureInfinite());
	}

	/**
	 * Set temporal attribute from now till future infinite
	 *
	 * @param attribute Attribute
	 */
	public void setCurrentTmpAttribute(@NotNull BuildingAttribute attribute) {
		setTmpAttributeForDate(attribute, DateUtil.now());
	}

	/**
	 * Set temporal attribute from <code>date</code> till future infinite
	 *
	 * @param attribute Attribute
	 * @param date	  attribute begin date
	 */
	public void setTmpAttributeForDate(@NotNull BuildingAttribute attribute, Date date) {
		setTmpAttributeForDates(attribute, date, ApplicationConfig.getFutureInfinite());
	}

	/**
	 * Set temporal attribute from <code>date</code> till future infinite
	 *
	 * @param attribute Attribute
	 * @param begin	 attribute begin date
	 * @param end	   attribute end date
	 */
	public void setTmpAttributeForDates(@NotNull BuildingAttribute attribute, Date begin, Date end) {
		if (attribute.notEmpty()) {
			attribute.setTemporal(1);
		}
		doSetAttributeForDates(attribute, begin, end);
	}

	private void doSetAttributeForDates(@NotNull BuildingAttribute attribute, Date begin, Date end) {

		if (begin.before(ApplicationConfig.getPastInfinite())) {
			begin = ApplicationConfig.getPastInfinite();
		}
		if (end.after(ApplicationConfig.getFutureInfinite())) {
			end = ApplicationConfig.getFutureInfinite();
		}
		begin = DateUtil.truncateDay(begin);
		end = DateUtil.truncateDay(end);

		SortedSet<BuildingAttribute> attrs = attributesOfType(attribute.getAttributeType());
		Set<BuildingAttribute> toDelete = CollectionUtils.set();
		Set<BuildingAttribute> toAdd = CollectionUtils.set();
		for (BuildingAttribute old : attrs) {
			old.setTemporal(attribute.getTemporal());
			if (DateIntervalUtil.areIntersecting(old.getBegin(), old.getEnd(), begin, end)) {
				if (old.getBegin().before(begin)) {
					BuildingAttribute copy = old.copy();
					copy.setEnd(DateUtil.previous(begin));
					toAdd.add(copy);
				}
				if (old.getEnd().after(end)) {
					BuildingAttribute copy = old.copy();
					copy.setBegin(DateUtil.next(end));
					toAdd.add(copy);
				}
				toDelete.add(old);
			}
		}

		if (attribute.notEmpty()) {
			attribute.setBegin(begin);
			attribute.setEnd(end);
			toAdd.add(attribute);
			attribute.setBuilding(this);
		}

		attrs.removeAll(toDelete);
		attributes.removeAll(toDelete);
		attrs.addAll(toAdd);
		attributes.addAll(toAdd);
	}

	@NotNull
	public SortedSet<BuildingAttribute> attributesOfType(BuildingAttributeType type) {

		Map<BuildingAttributeType, SortedSet<BuildingAttribute>> splittedAttributes = splitAttributes();
		SortedSet<BuildingAttribute> attrs = splittedAttributes.get(type);
		if (attrs == null) {
			attrs = CollectionUtils.treeSet();
			splittedAttributes.put(type, attrs);
		}

		return attrs;
	}

	public Set<BuildingAttributeType> attributeTypes() {
		return splitAttributes().keySet();
	}

	public List<BuildingAttribute> currentAttributes() {
		List<BuildingAttribute> result = CollectionUtils.list();
		Date now = DateUtil.now();
		for (BuildingAttribute attribute : attributes) {
			if (DateIntervalUtil.includes(now, attribute.getBegin(), attribute.getEnd())) {
				result.add(attribute);
			}
		}

		return result;
	}
}
