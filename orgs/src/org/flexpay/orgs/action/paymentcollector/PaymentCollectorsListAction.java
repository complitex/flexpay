package org.flexpay.orgs.action.paymentcollector;

import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

public class PaymentCollectorsListAction extends FPActionWithPagerSupport<PaymentCollector> {

	private List<PaymentCollector> collectors = CollectionUtils.list();

	private OrganizationService organizationService;
	private PaymentCollectorService collectorService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		collectors = collectorService.listInstances(getPager());

		return SUCCESS;
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
		return SUCCESS;
	}

	public String getOrganizationName(@Nullable Organization org) throws FlexPayException {
		if (org == null || org.isNew()) {
			return "---";
		}
		Organization persistent = organizationService.readFull(stub(org));
		if (persistent == null) {
			log.warn("Invalid organisation requested {}", org);
			return "N/A";
		}
		return getTranslation(persistent.getNames()).getName();
	}

	public List<PaymentCollector> getCollectors() {
		return collectors;
	}

	@Required
	public void setCollectorService(PaymentCollectorService collectorService) {
		this.collectorService = collectorService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

}
