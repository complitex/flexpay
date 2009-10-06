package org.flexpay.payments.actions.service;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.ServiceTypeNameTranslation;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class ServiceTypeEditAction extends FPActionSupport {

	private ServiceType serviceType = new ServiceType();
	private Map<Long, String> names = map();
	private Map<Long, String> descriptions = map();

	private String crumbCreateKey;
	private ServiceTypeService serviceTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		ServiceType type = serviceType.isNotNew() ? serviceTypeService.read(stub(serviceType)) : serviceType;
		if (type == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		if (isNotSubmit()) {
			serviceType = type;
			initNames();
			return INPUT;
		}

		type.setCode(serviceType.getCode());

		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			ServiceTypeNameTranslation nameTranslation = new ServiceTypeNameTranslation(value, lang);
			nameTranslation.setDescription(descriptions.get(name.getKey()));
			type.setTypeName(nameTranslation);
		}

		if (type.isNew()) {
			serviceTypeService.create(type);
		} else {
			serviceTypeService.update(type);
		}

		addActionMessage(getText("payments.service_type.saved"));

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

	private void initNames() {
		for (ServiceTypeNameTranslation name : serviceType.getTypeNames()) {
			names.put(name.getLang().getId(), name.getName());
			descriptions.put(name.getLang().getId(), name.getDescription());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (names.containsKey(lang.getId())) {
				continue;
			}
			names.put(lang.getId(), "");
			descriptions.put(lang.getId(), "");
		}
	}

	@Override
	protected void setBreadCrumbs() {
		if (serviceType.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public Map<Long, String> getNames() {
		return names;
	}

	public void setNames(Map<Long, String> names) {
		this.names = names;
	}

	public Map<Long, String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Map<Long, String> descriptions) {
		this.descriptions = descriptions;
	}

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

}
