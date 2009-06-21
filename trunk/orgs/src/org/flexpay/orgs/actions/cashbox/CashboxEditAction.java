package org.flexpay.orgs.actions.cashbox;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.CashboxNameTranslation;
import org.flexpay.orgs.persistence.filters.PaymentPointsFilter;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.actions.CashboxCookieActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class CashboxEditAction extends FPActionSupport {

	private PaymentPointsFilter paymentPointsFilter = new PaymentPointsFilter();

	private Cashbox cashbox = new Cashbox();
	private Map<Long, String> names = map();

	private String crumbCreateKey;
	private CashboxService cashboxService;
	private PaymentPointService paymentPointService;

	public CashboxEditAction() {
		paymentPointsFilter.setNeedAutoChange(false);
	}

	@NotNull
	protected String doExecute() throws Exception {

		if (cashbox.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}

		Cashbox oldCashbox = cashbox.isNotNew() ? cashboxService.read(stub(cashbox)) : cashbox;
		if (oldCashbox == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		paymentPointService.initFilter(paymentPointsFilter);

		if (!isSubmit()) {
			cashbox = oldCashbox;
			initNames();
			if (cashbox.isNotNew()) {
				paymentPointsFilter.setSelectedId(cashbox.getPaymentPoint().getId());
			}
			return INPUT;
		}

		if (!paymentPointsFilter.needFilter()) {
			addActionError(getText("eirc.error.cashbox.no_payment_point"));
			return INPUT;
		}

		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			CashboxNameTranslation nameTranslation = new CashboxNameTranslation();
			nameTranslation.setLang(lang);
			nameTranslation.setName(value);

			log.debug("Setting cashbox name: {}", nameTranslation);

			oldCashbox.setName(nameTranslation);
		}

		oldCashbox.setPaymentPoint(paymentPointService.read(paymentPointsFilter.getSelectedStub()));

		if (oldCashbox.isNew()) {
			cashboxService.create(oldCashbox);
		} else {
			cashboxService.update(oldCashbox);
		}

		return REDIRECT_SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		return REDIRECT_SUCCESS;
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

	public PaymentPointsFilter getPaymentPointsFilter() {
		return paymentPointsFilter;
	}

	public void setPaymentPointsFilter(PaymentPointsFilter paymentPointsFilter) {
		this.paymentPointsFilter = paymentPointsFilter;
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
