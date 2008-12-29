package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.eirc.persistence.PaymentsCollector;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.service.PaymentsCollectorService;
import org.flexpay.eirc.service.OrganizationService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class PaymentsCollectorsListAction extends FPActionSupport {

	private OrganizationService organizationService;
	private PaymentsCollectorService collectorService;

	private Page<PaymentsCollector> pager = new Page<PaymentsCollector>();
	private List<PaymentsCollector> instances = Collections.emptyList();

	@NotNull
	public String doExecute() throws Exception {

		instances = collectorService.listInstances(pager);

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
	

	public Page<PaymentsCollector> getPager() {
		return pager;
	}

	public void setPager(Page<PaymentsCollector> pager) {
		this.pager = pager;
	}

	public List<PaymentsCollector> getInstances() {
		return instances;
	}

	@Required
	public void setCollectorService(PaymentsCollectorService collectorService) {
		this.collectorService = collectorService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}