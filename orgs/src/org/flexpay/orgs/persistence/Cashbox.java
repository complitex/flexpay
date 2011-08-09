package org.flexpay.orgs.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.set;

public class Cashbox extends DomainObjectWithStatus {

	private Set<CashboxNameTranslation> names = set();

	private PaymentPoint paymentPoint;
	private Long tradingDayProcessInstanceId;

    public Cashbox() {
    }

    public Cashbox(@NotNull Long id) {
        super(id);
    }

    public Set<CashboxNameTranslation> getNames() {
		return names;
	}

	public void setNames(Set<CashboxNameTranslation> names) {
		this.names = names;
	}

	public PaymentPoint getPaymentPoint() {
		return paymentPoint;
	}

	public Stub<PaymentPoint> getPaymentPointStub() {
		return stub(paymentPoint);
	}

	public void setPaymentPoint(PaymentPoint paymentPoint) {
		this.paymentPoint = paymentPoint;
	}

	public Long getTradingDayProcessInstanceId() {
		return tradingDayProcessInstanceId;
	}

	public void setTradingDayProcessInstanceId(Long tradingDayProcessInstanceId) {
		this.tradingDayProcessInstanceId = tradingDayProcessInstanceId;
	}

	public void setName(CashboxNameTranslation name) {
		names = TranslationUtil.setTranslation(names, this, name);
	}

	public String getName(@NotNull Locale locale) {
		return TranslationUtil.getTranslation(names, locale).getName();
	}

	public String getName() {
		return getName(ApplicationConfig.getDefaultLocale());
	}

	/**
	 * Get name translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public CashboxNameTranslation getTranslation(@NotNull Language lang) {

		for (CashboxNameTranslation translation : getNames()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				append("tradingDayProcessInstanceId", getTradingDayProcessInstanceId()).
				toString();
	}

}
