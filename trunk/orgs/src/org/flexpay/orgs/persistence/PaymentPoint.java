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

/**
 * Place where payments are taken
 */
public class PaymentPoint extends DomainObjectWithStatus {

	private String address;
//    private org.jbpm.graph.exe.ProcessInstance tradingDayProcessInstance;
    private Long tradingDayProcessInstanceId;

	private PaymentCollector collector;
	private Set<PaymentPointName> names = set();

    public PaymentPoint() {
	}

	public PaymentPoint(@NotNull Long id) {
		super(id);
	}

	public PaymentPoint(@NotNull Stub<PaymentPoint> stub) {
		super(stub.getId());
	}

	public PaymentCollector getCollector() {
		return collector;
	}

	public void setCollector(PaymentCollector collector) {
		this.collector = collector;
	}

    public Stub<PaymentCollector> collectorStub() {
        return stub(collector);
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

//    public ProcessInstance getTradingDayProcessInstance() {
//        return tradingDayProcessInstance;
//    }
//
//    public void setTradingDayProcessInstance(ProcessInstance tradingDayProcessInstance) {
//        this.tradingDayProcessInstance = tradingDayProcessInstance;
//    }

    public Long getTradingDayProcessInstanceId() {
        return tradingDayProcessInstanceId;
    }

    public void setTradingDayProcessInstanceId(Long tradingDayProcessInstanceId) {
        this.tradingDayProcessInstanceId = tradingDayProcessInstanceId;
    }

    /**
	 * Get name translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public PaymentPointName getTranslation(@NotNull Language lang) {

		for (PaymentPointName translation : getNames()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", id).
				append("status", status).
				append("address", address).
                append("tradingDayProcessInstanceId", tradingDayProcessInstanceId).
				toString();
	}
}
