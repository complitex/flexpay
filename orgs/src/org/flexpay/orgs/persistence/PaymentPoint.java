package org.flexpay.orgs.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

/**
 * Place where payments are taken
 */
public class PaymentPoint extends DomainObjectWithStatus {

	private String address;

	private PaymentsCollector collector;
	private Set<PaymentPointName> names = Collections.emptySet();

	public PaymentPoint() {
	}

	public PaymentPoint(@NotNull Long id) {
		super(id);
	}

	public PaymentPoint(@NotNull Stub<PaymentPoint> stub) {
		super(stub.getId());
	}

	public PaymentsCollector getCollector() {
		return collector;
	}

	public void setCollector(PaymentsCollector collector) {
		this.collector = collector;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<PaymentPointName> getNames() {
		return names;
	}

	public void setNames(Set<PaymentPointName> names) {
		this.names = names;
	}

	public void setName(PaymentPointName name) {
		names = TranslationUtil.setTranslation(names, this, name);
	}

	public String getName(@NotNull Locale locale) {
		return TranslationUtil.getTranslation(names, locale).getName();
	}

	public String getName() {
		return getName(ApplicationConfig.getDefaultLocale());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				append("address", address).
				toString();
	}

}
