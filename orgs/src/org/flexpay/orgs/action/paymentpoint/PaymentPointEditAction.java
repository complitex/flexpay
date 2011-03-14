package org.flexpay.orgs.action.paymentpoint;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.PaymentPointName;
import org.flexpay.orgs.persistence.filters.PaymentCollectorFilter;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class PaymentPointEditAction extends FPActionSupport {

	private PaymentCollectorFilter paymentCollectorFilter = new PaymentCollectorFilter();
	private PaymentPoint point = new PaymentPoint();
	private Map<Long, String> names = map();

	private String crumbCreateKey;
	private PaymentCollectorService paymentCollectorService;
	private PaymentPointService paymentPointService;

	public PaymentPointEditAction() {
		paymentCollectorFilter.setAllowEmpty(false);
		paymentCollectorFilter.setNeedAutoChange(false);
	}

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	@Override
	protected String doExecute() throws Exception {

		String address = "";
		if (point.isNotNew() && isSubmit()) {
			address = "" + point.getAddress();
		}
		point = point.isNew() ? point : paymentPointService.read(stub(point));
		if (point == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		if (point.isNotNew() && isSubmit()) {
			point.setAddress(address);
		}

		paymentCollectorService.initFilter(paymentCollectorFilter);

		if (isNotSubmit()) {
			initNames();
			if (point.isNotNew()) {
				paymentCollectorFilter.setSelectedId(point.getCollector().getId());
				paymentCollectorFilter.setReadOnly(true);
			}

			return INPUT;
		}

		if (!paymentCollectorFilter.needFilter()) {
			addActionError(getText("eirc.error.payment_point.no_collector"));
			return INPUT;
		}

		if (point.isNew()) {
			point.setCollector(paymentCollectorService.read(paymentCollectorFilter.getSelectedStub()));
		}

		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			point.setName(new PaymentPointName(value, lang));
		}

		if (point.isNew()) {
			paymentPointService.create(point);
		} else {
			paymentPointService.update(point);
		}

		return REDIRECT_SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	@Override
	protected void setBreadCrumbs() {
		if (point.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	private void initNames() {
		for (PaymentPointName name : point.getNames()) {
			names.put(name.getLang().getId(), name.getName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (names.containsKey(lang.getId())) {
				continue;
			}
			names.put(lang.getId(), "");
		}
	}

	public PaymentCollectorFilter getPaymentCollectorFilter() {
		return paymentCollectorFilter;
	}

	public void setPaymentCollectorFilter(PaymentCollectorFilter paymentCollectorFilter) {
		this.paymentCollectorFilter = paymentCollectorFilter;
	}

	public Map<Long, String> getNames() {
		return names;
	}

	public void setNames(Map<Long, String> names) {
		this.names = names;
	}

	public PaymentPoint getPoint() {
		return point;
	}

	public void setPoint(PaymentPoint point) {
		this.point = point;
	}

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	@Required
	public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
		this.paymentCollectorService = paymentCollectorService;
	}

}
