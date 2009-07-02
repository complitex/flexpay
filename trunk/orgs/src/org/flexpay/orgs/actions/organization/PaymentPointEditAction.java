package org.flexpay.orgs.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.orgs.persistence.PaymentPointName;
import org.flexpay.orgs.persistence.filters.PaymentsCollectorFilter;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.PaymentsCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class PaymentPointEditAction extends FPActionSupport {

	private PaymentsCollectorFilter paymentsCollectorFilter = new PaymentsCollectorFilter();
	private PaymentPoint point = new PaymentPoint();
	private Map<Long, String> names = map();

	private OrganizationHelper organizationHelper;
	private PaymentsCollectorService paymentsCollectorService;
	private PaymentPointService paymentPointService;

	public PaymentPointEditAction() {
		paymentsCollectorFilter.setAllowEmpty(false);
		paymentsCollectorFilter.setNeedAutoChange(false);
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
	protected String doExecute() throws Exception {

		if (point.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}

		PaymentPoint pnt = point.isNew() ? point : paymentPointService.read(stub(point));
		if (pnt == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		paymentsCollectorService.initFilter(paymentsCollectorFilter);

		// prepare initial setup
		if (!isSubmit()) {
			point = pnt;
			initNames();
			if (pnt.isNotNew()) {
				paymentsCollectorFilter.setSelectedId(pnt.getCollector().getId());
				paymentsCollectorFilter.setReadOnly(true);
			}

			return INPUT;
		}

		if (pnt.isNew()) {
			pnt.setCollector(paymentsCollectorService.read(paymentsCollectorFilter.getSelectedStub()));
		}
		pnt.setAddress(point.getAddress());

		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			PaymentPointName nameTranslation = new PaymentPointName();
			nameTranslation.setLang(lang);
			nameTranslation.setName(value);

			log.debug("Setting payment point name: {}", nameTranslation);

			pnt.setName(nameTranslation);
		}

		if (pnt.isNew()) {
			paymentPointService.create(pnt);
		} else {
			paymentPointService.update(pnt);
		}

		return REDIRECT_SUCCESS;
	}

	public String getCollectorName(@NotNull PaymentsCollector collectorStub) {
		return organizationHelper.getName(collectorStub, userPreferences.getLocale());
	}

	public String getCollectorName(@NotNull Organization organizationStub) {
		return organizationHelper.getName(organizationStub, userPreferences.getLocale());
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

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return INPUT;
	}

	public PaymentsCollectorFilter getPaymentsCollectorFilter() {
		return paymentsCollectorFilter;
	}

	public void setPaymentsCollectorFilter(PaymentsCollectorFilter paymentsCollectorFilter) {
		this.paymentsCollectorFilter = paymentsCollectorFilter;
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

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	@Required
	public void setPaymentsCollectorService(PaymentsCollectorService paymentsCollectorService) {
		this.paymentsCollectorService = paymentsCollectorService;
	}

	@Required
	public void setOrganizationHelper(OrganizationHelper organizationHelper) {
		this.organizationHelper = organizationHelper;
	}

}
