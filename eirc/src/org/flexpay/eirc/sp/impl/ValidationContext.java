package org.flexpay.eirc.sp.impl;

import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.common.persistence.Stub;

import java.util.Date;

public class ValidationContext {

	private Date from;
	private Date till;
	private Long serviceProviderId;

	public Long getServiceProviderId() {
		return serviceProviderId;
	}

	public Stub<ServiceProvider> getServiceProviderStub() {
		return new Stub<ServiceProvider>(serviceProviderId);
	}

	public void setServiceProviderId(Long serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTill() {
		return till;
	}

	public void setTill(Date till) {
		this.till = till;
	}
}
