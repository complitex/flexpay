package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.apache.commons.lang.StringUtils;

import java.util.Set;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

public class Service extends DomainObject {

	private ServiceProvider serviceProvider;
	private Set<ServiceDescription> descriptions = Collections.emptySet();
	private ServiceType serviceType;
	private String externalCode;
	private Date beginDate;
	private Date endDate;
	private Service parentService;
	private Set<Service> childServices = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	public Service() {
	}

	public Service(Long id) {
		super(id);
	}

	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public Set<ServiceDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set<ServiceDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public String getExternalCode() {
		return externalCode;
	}

	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setDescription(ServiceDescription serviceDescription) {
		if (descriptions == Collections.EMPTY_SET) {
			descriptions = new HashSet<ServiceDescription>();
		}

		ServiceDescription candidate = null;
		for (ServiceDescription description : descriptions) {
			if (description.getLang().getId().equals(serviceDescription.getLang().getId())) {
				candidate = description;
				break;
			}
		}

		if (candidate != null) {
			if (StringUtils.isBlank(serviceDescription.getName())) {
				descriptions.remove(candidate);
				return;
			}
			candidate.setName(serviceDescription.getName());
			return;
		}

		if (StringUtils.isBlank(serviceDescription.getName())) {
			return;
		}

		serviceDescription.setTranslatable(this);
		descriptions.add(serviceDescription);
	}

	public Service getParentService() {
		return parentService;
	}

	public void setParentService(Service parentService) {
		this.parentService = parentService;
	}

	/**
	 * Check if this service is a subservice
	 *
	 * @return <code>true</code> if parent service is not <code>null</code>, or <code>false</code> otherwise
	 */
	public boolean isSubService() {
		return parentService != null;
	}

	public Set<Service> getChildServices() {
		return childServices;
	}

	public void setChildServices(Set<Service> childServices) {
		this.childServices = childServices;
	}
}
