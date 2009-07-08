package org.flexpay.eirc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.eirc.persistence.consumer.ConsumerAttribute;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.payments.persistence.Service;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Consumer extends DomainObjectWithStatus {

	private String externalAccountNumber;
	private Date beginDate;
	private Date endDate;

	private Apartment apartment;
	private ConsumerInfo consumerInfo;
	private EircAccount eircAccount;
	private Service service;
	private Person responsiblePerson;

	private Set<ConsumerAttribute> attributes = Collections.emptySet();

	public Consumer() {
	}

	public Consumer(Long id) {
		super(id);
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public String getExternalAccountNumber() {
		return externalAccountNumber;
	}

	public void setExternalAccountNumber(String externalAccountNumber) {
		this.externalAccountNumber = externalAccountNumber;
	}

	@Nullable
	public Person getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(@Nullable Person responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public ConsumerInfo getConsumerInfo() {
		return consumerInfo;
	}

	public void setConsumerInfo(ConsumerInfo consumerInfo) {
		this.consumerInfo = consumerInfo;
	}

	public EircAccount getEircAccount() {
		return eircAccount;
	}

	public void setEircAccount(EircAccount eircAccount) {
		this.eircAccount = eircAccount;
	}

	public Stub<EircAccount> getEircAccountStub() {
		return stub(eircAccount);
	}

	public Stub<Apartment> getApartmentStub() {
		return stub(apartment);
	}

	@NotNull
	public Stub<Service> getServiceStub() {
		return stub(getService());
	}

	public Set<ConsumerAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<ConsumerAttribute> attributes) {
		this.attributes = attributes;
		splitCache = null;
	}

	private Map<ConsumerAttributeTypeBase, SortedSet<ConsumerAttribute>> splitCache = null;

	private Map<ConsumerAttributeTypeBase, SortedSet<ConsumerAttribute>> splitAttributes() {

		if (splitCache != null) {
			return splitCache;
		}

		Map<ConsumerAttributeTypeBase, SortedSet<ConsumerAttribute>> result = CollectionUtils.map();
		for (ConsumerAttribute attribute : getAttributes()) {
			ConsumerAttributeTypeBase type = attribute.getType();
			SortedSet<ConsumerAttribute> group = result.get(type);
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
	public ConsumerAttribute getAttribute(ConsumerAttributeTypeBase attributeType) {
		return getAttributeForDate(attributeType, DateUtil.now());
	}

	@Nullable
	public ConsumerAttribute getAttributeForDate(ConsumerAttributeTypeBase attributeType, Date date) {

		SortedSet<ConsumerAttribute> attrs = findAttributes(attributeType);
		for (ConsumerAttribute attribute : attrs) {
			if (DateIntervalUtil.includes(date, attribute.getBegin(), attribute.getEnd())) {
				return attribute;
			}
		}

		return null;
	}

	/**
	 * Set normal consumer attribute
	 *
	 * @param attribute Consumer attribute
	 */
	public void setNormalAttribute(@NotNull ConsumerAttribute attribute) {
		if (attribute.notEmpty()) {
			attribute.setTemporal(0);
		}
		doSetAttributeForDates(attribute, ApplicationConfig.getPastInfinite(), ApplicationConfig.getFutureInfinite());
	}

	/**
	 * Set temporal consumer attribute from now till future infinite
	 *
	 * @param attribute Consumer attribute
	 */
	public void setCurrentTmpAttribute(@NotNull ConsumerAttribute attribute) {
		setTmpAttributeForDate(attribute, DateUtil.now());
	}

	/**
	 * Set temporal consumer attribute from <code>date</code> till future infinite
	 *
	 * @param attribute Consumer attribute
	 * @param date	  attribute begin date
	 */
	public void setTmpAttributeForDate(@NotNull ConsumerAttribute attribute, Date date) {
		setTmpAttributeForDates(attribute, date, ApplicationConfig.getFutureInfinite());
	}

	/**
	 * Set temporal consumer attribute from <code>date</code> till future infinite
	 *
	 * @param attribute Consumer attribute
	 * @param begin	 attribute begin date
	 * @param end	   attribute end date
	 */
	public void setTmpAttributeForDates(@NotNull ConsumerAttribute attribute, Date begin, Date end) {
		if (attribute.notEmpty()) {
			attribute.setTemporal(1);
		}
		doSetAttributeForDates(attribute, begin, end);
	}

	private void doSetAttributeForDates(@NotNull ConsumerAttribute attribute, Date begin, Date end) {

		if (begin.before(ApplicationConfig.getPastInfinite())) {
			begin = ApplicationConfig.getPastInfinite();
		}
		if (end.after(ApplicationConfig.getFutureInfinite())) {
			end = ApplicationConfig.getFutureInfinite();
		}
		begin = DateUtil.truncateDay(begin);
		end = DateUtil.truncateDay(end);

		SortedSet<ConsumerAttribute> attrs = findAttributes(attribute.getType());
		Set<ConsumerAttribute> toDelete = CollectionUtils.set();
		Set<ConsumerAttribute> toAdd = CollectionUtils.set();
		for (ConsumerAttribute old : attrs) {
			old.setTemporal(attribute.getTemporal());
			if (DateIntervalUtil.areIntersecting(old.getBegin(), old.getEnd(), begin, end)) {
				if (old.getBegin().before(begin)) {
					ConsumerAttribute copy = old.copy();
					copy.setEnd(DateUtil.previous(begin));
					toAdd.add(copy);
				}
				if (old.getEnd().after(end)) {
					ConsumerAttribute copy = old.copy();
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
			attribute.setConsumer(this);
		}

		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (attributes == Collections.EMPTY_SET) {
			attributes = CollectionUtils.set();
		}

		boolean deleted = attrs.removeAll(toDelete);
		deleted |= attributes.removeAll(toDelete);
		attrs.addAll(toAdd);
		attributes.addAll(toAdd);
	}

	@NotNull
	private SortedSet<ConsumerAttribute> findAttributes(ConsumerAttributeTypeBase type) {

		Map<ConsumerAttributeTypeBase, SortedSet<ConsumerAttribute>> splittedAttributes = splitAttributes();
		SortedSet<ConsumerAttribute> attrs = splittedAttributes.get(type);
		if (attrs == null) {
			attrs = CollectionUtils.treeSet();
			splittedAttributes.put(type, attrs);
		}

		return attrs;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("externalAccountNumber", externalAccountNumber).
				append("beginDate", beginDate).
				append("endDate", endDate).
				toString();
	}

	public Consumer copy() {

		Consumer consumer = new Consumer();
		consumer.setExternalAccountNumber(getExternalAccountNumber());
		consumer.setEircAccount(getEircAccount());
		consumer.setService(getService());
		consumer.setApartment(getApartment());
		consumer.setBeginDate(getBeginDate());
		consumer.setEndDate(getEndDate());
		
		return consumer;
	}
}
