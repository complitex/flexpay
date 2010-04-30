package org.flexpay.payments.persistence;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static org.flexpay.common.persistence.Stub.stub;

public class Service extends DomainObjectWithStatus {

    private String externalCode;
    private Date beginDate;
    private Date endDate;

	private ServiceProvider serviceProvider;
	private ServiceType serviceType;
	private MeasureUnit measureUnit;
	private Service parentService;

    private Set<ServiceDescription> descriptions = Collections.emptySet();
	private Set<Service> childServices = Collections.emptySet();

	public Service() {
	}

	public Service(Long id) {
		super(id);
	}

	public Service(@NotNull Stub<Service> stub) {
		super(stub.getId());
	}

	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	@NotNull
	public Stub<ServiceProvider> providerStub() {
		return stub(serviceProvider);
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

	/**
	 * Get flexpay registry representation of this service code, i.e. if external code available this is '#'+externalCode
	 * otherwise this is service id
	 *
	 * @return FlexPay registry format service code
	 */
	public String registryCode() {
		return StringUtils.isBlank(externalCode) ? String.valueOf(getId()) : "#" + externalCode;
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
		if (Collections.emptySet().equals(descriptions)) {
			descriptions = new HashSet<ServiceDescription>();
		}

		ServiceDescription candidate = null;
		for (ServiceDescription description : descriptions) {
			if (description.isSameLanguage(serviceDescription)) {
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
	 * Get parent service stub, call only when {@link #isSubService()} is <code>true</code>
	 *
	 * @return Parent service stub
	 */
	public Stub<Service> parentServiceStub() {
		return stub(parentService);
	}

	/**
	 * Check if this service is a subservice
	 *
	 * @return <code>true</code> if parent service is not <code>null</code>, or <code>false</code> otherwise
	 */
	public boolean isSubService() {
		return parentService != null;
	}

	public boolean isNotSubservice() {
		return !isSubService();
	}

	public Set<Service> getChildServices() {
		return childServices;
	}

	public void setChildServices(Set<Service> childServices) {
		this.childServices = childServices;
	}

	public MeasureUnit getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(MeasureUnit measureUnit) {
		this.measureUnit = measureUnit;
	}

	public boolean hasMeasureUnit() {
		return measureUnit != null;
	}

	/**
	 * Get measure unit stub, call only if {@link #hasMeasureUnit()} is <code>true</code>
	 *
	 * @return Measure unit stub
	 */
	@NotNull
	public Stub<MeasureUnit> measureUnitStub() {
		return stub(measureUnit);
	}

	@NotNull
	public String format(Locale locale) throws Exception {
		ServiceDescription description = TranslationUtil.getTranslation(descriptions, locale);
		return description != null ? description.getName() : "";
	}

	public Stub<ServiceType> serviceTypeStub() {
		return new Stub<ServiceType>(serviceType);
	}

	/**
	 * Get description translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public ServiceDescription getDescription(@NotNull Language lang) {

		for (ServiceDescription translation : getDescriptions()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}
}
