package org.flexpay.orgs.action.cashbox;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.CashboxNameTranslation;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.map;

public class CashboxEditAction extends FPActionSupport {

	private PaymentPointFilter paymentPointFilter = new PaymentPointFilter();

	private Cashbox cashbox = new Cashbox();
	private Map<Long, String> names = map();

	private String crumbCreateKey;
	private CashboxService cashboxService;
	private PaymentPointService paymentPointService;

	public CashboxEditAction() {
		paymentPointFilter.setAllowEmpty(false);
		paymentPointFilter.setNeedAutoChange(false);
	}

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		cashbox = cashbox.isNew() ? cashbox : cashboxService.read(stub(cashbox));
		if (cashbox == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		paymentPointService.initFilter(paymentPointFilter);

		if (isNotSubmit()) {
			initNames();
			if (cashbox.isNotNew()) {
				paymentPointFilter.setSelectedId(cashbox.getPaymentPoint().getId());
			}
			return INPUT;
		}

		if (!paymentPointFilter.needFilter()) {
			addActionError(getText("eirc.error.cashbox.no_payment_point"));
			return INPUT;
		}

		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			cashbox.setName(new CashboxNameTranslation(value, lang));
		}

		cashbox.setPaymentPoint(paymentPointService.read(paymentPointFilter.getSelectedStub()));

		if (cashbox.isNew()) {
			cashboxService.create(cashbox);
		} else {
			cashboxService.update(cashbox);
		}

		return REDIRECT_SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	@Override
	protected void setBreadCrumbs() {
		if (cashbox.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	private void initNames() {
		for (CashboxNameTranslation name : cashbox.getNames()) {
			names.put(name.getLang().getId(), name.getName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (names.containsKey(lang.getId())) {
				continue;
			}
			names.put(lang.getId(), "");
		}
	}

	public Cashbox getCashbox() {
		return cashbox;
	}

	public void setCashbox(Cashbox cashbox) {
		this.cashbox = cashbox;
	}

	public Map<Long, String> getNames() {
		return names;
	}

	public void setNames(Map<Long, String> names) {
		this.names = names;
	}

	public PaymentPointFilter getPaymentPointFilter() {
		return paymentPointFilter;
	}

	public void setPaymentPointFilter(PaymentPointFilter paymentPointFilter) {
		this.paymentPointFilter = paymentPointFilter;
	}

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

}
