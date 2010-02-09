package org.flexpay.payments.persistence.config;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.ServiceType;

import static org.flexpay.common.persistence.Stub.stub;

public class MbServiceTypeMapping extends DomainObject {

	private String mbServiceCode;
	private String mbServiceName;
	private ServiceType serviceType;

	public String getMbServiceCode() {
		return mbServiceCode;
	}

	public void setMbServiceCode(String mbServiceCode) {
		this.mbServiceCode = mbServiceCode;
	}

	public String getMbServiceName() {
		return mbServiceName;
	}

	public void setMbServiceName(String mbServiceName) {
		this.mbServiceName = mbServiceName;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public Stub<ServiceType> serviceTypeStub() {
		return stub(serviceType);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("mbServiceCode", mbServiceCode).
				append("mbServiceName", mbServiceName).
				append("serviceType-id", serviceType != null ? serviceType.getId() : null).
				toString();
	}
}
