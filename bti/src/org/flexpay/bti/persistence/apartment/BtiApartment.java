package org.flexpay.bti.persistence.apartment;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static org.flexpay.common.util.CollectionUtils.set;

public class BtiApartment extends Apartment {

	private Set<ApartmentAttribute> attributes = set();

	protected BtiApartment() {
	}

	public BtiApartment(@NotNull Long id) {
		super(id);
	}

	public BtiApartment(@NotNull Stub<BtiApartment> stub) {
		super(stub.getId());
	}

	public static BtiApartment newInstance() {
		return new BtiApartment();
	}

	public Set<ApartmentAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<ApartmentAttribute> attributes) {
		this.attributes = attributes;
	}

	private Map<ApartmentAttributeType, SortedSet<ApartmentAttribute>> splitCache = null;

	private Map<ApartmentAttributeType, SortedSet<ApartmentAttribute>> splitAttributes() {

		if (splitCache != null) {
			return splitCache;
		}

		Map<ApartmentAttributeType, SortedSet<ApartmentAttribute>> result = CollectionUtils.map();
		for (ApartmentAttribute attribute : getAttributes()) {
			ApartmentAttributeType type = attribute.getAttributeType();
			SortedSet<ApartmentAttribute> group = result.get(type);
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
	public ApartmentAttribute getCurrentAttribute(ApartmentAttributeType attributeType) {
		return getAttributeForDate(attributeType, DateUtil.now());
	}

	@Nullable
	public ApartmentAttribute getAttributeForDate(ApartmentAttributeType attributeType, Date date) {

		SortedSet<ApartmentAttribute> attrs = findAttributes(attributeType);
		for (ApartmentAttribute attribute : attrs) {
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
	public void setNormalAttribute(@NotNull ApartmentAttribute attribute) {
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
	public void setCurrentTmpAttribute(@NotNull ApartmentAttribute attribute) {
		setTmpAttributeForDate(attribute, DateUtil.now());
	}

	/**
	 * Set temporal attribute from <code>date</code> till future infinite
	 *
	 * @param attribute Attribute
	 * @param date	  attribute begin date
	 */
	public void setTmpAttributeForDate(@NotNull ApartmentAttribute attribute, Date date) {
		setTmpAttributeForDates(attribute, date, ApplicationConfig.getFutureInfinite());
	}

	/**
	 * Set temporal attribute from <code>date</code> till future infinite
	 *
	 * @param attribute Attribute
	 * @param begin	 attribute begin date
	 * @param end	   attribute end date
	 */
	public void setTmpAttributeForDates(@NotNull ApartmentAttribute attribute, Date begin, Date end) {
		if (attribute.notEmpty()) {
			attribute.setTemporal(1);
		}
		doSetAttributeForDates(attribute, begin, end);
	}

	private void doSetAttributeForDates(@NotNull ApartmentAttribute attribute, Date begin, Date end) {

		if (begin.before(ApplicationConfig.getPastInfinite())) {
			begin = ApplicationConfig.getPastInfinite();
		}
		if (end.after(ApplicationConfig.getFutureInfinite())) {
			end = ApplicationConfig.getFutureInfinite();
		}
		begin = DateUtil.truncateDay(begin);
		end = DateUtil.truncateDay(end);

		SortedSet<ApartmentAttribute> attrs = findAttributes(attribute.getAttributeType());
		Set<ApartmentAttribute> toDelete = CollectionUtils.set();
		Set<ApartmentAttribute> toAdd = CollectionUtils.set();
		for (ApartmentAttribute old : attrs) {
			old.setTemporal(attribute.getTemporal());
			if (DateIntervalUtil.areIntersecting(old.getBegin(), old.getEnd(), begin, end)) {
				if (old.getBegin().before(begin)) {
					ApartmentAttribute copy = old.copy();
					copy.setEnd(DateUtil.previous(begin));
					toAdd.add(copy);
				}
				if (old.getEnd().after(end)) {
					ApartmentAttribute copy = old.copy();
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
			attribute.setApartment(this);
		}

		attrs.removeAll(toDelete);
		attributes.removeAll(toDelete);
		attrs.addAll(toAdd);
		attributes.addAll(toAdd);
	}

	@NotNull
	private SortedSet<ApartmentAttribute> findAttributes(ApartmentAttributeType type) {

		Map<ApartmentAttributeType, SortedSet<ApartmentAttribute>> splittedAttributes = splitAttributes();
		SortedSet<ApartmentAttribute> attrs = splittedAttributes.get(type);
		if (attrs == null) {
			attrs = CollectionUtils.treeSet();
			splittedAttributes.put(type, attrs);
		}

		return attrs;
	}

	public List<ApartmentAttribute> currentAttributes() {
		List<ApartmentAttribute> result = CollectionUtils.list();
		Date now = DateUtil.now();
		for (ApartmentAttribute attribute : attributes) {
			if (DateIntervalUtil.includes(now, attribute.getBegin(), attribute.getEnd())) {
				result.add(attribute);
			}
		}

		return result;
	}
}
