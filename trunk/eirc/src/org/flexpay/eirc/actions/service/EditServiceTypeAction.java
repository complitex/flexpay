package org.flexpay.eirc.actions.service;

import org.apache.log4j.Logger;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.ServiceTypeNameTranslation;
import org.flexpay.eirc.service.ServiceTypeService;

import java.util.HashMap;
import java.util.Map;

public class EditServiceTypeAction extends FPActionSupport {

	private Logger log = Logger.getLogger(getClass());

	private ServiceTypeService serviceTypeService;

	private ServiceType serviceType = new ServiceType();
	private Map<Long, String> names = new HashMap<Long, String>();
	private Map<Long, String> descriptions = new HashMap<Long, String>();

	public String execute() throws Exception {

		if (serviceType.getId() == null) {
			// todo: notify that no object was selected
			addActionError("No object was selected");
			return SUCCESS;
		}

		ServiceType type = serviceTypeService.read(serviceType);

		if (!isPost()) {
			serviceType = type;
			initNames();
			return INPUT;
		}

		type.setCode(serviceType.getCode());

		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			ServiceTypeNameTranslation nameTranslation = new ServiceTypeNameTranslation();
			nameTranslation.setLang(lang);
			nameTranslation.setName(value);
			nameTranslation.setDescription(descriptions.get(name.getKey()));

			if (log.isDebugEnabled()) {
				log.debug("Setting type name: " + nameTranslation);
			}

			type.setTypeName(nameTranslation);
		}

		try {
			serviceTypeService.save(type);
		} catch (FlexPayExceptionContainer container) {
			addActionErrors(container);
			return INPUT;
		}

		return SUCCESS;
	}

	private void initNames() {
		for (ServiceTypeNameTranslation name : serviceType.getTypeNames()) {
			names.put(name.getLang().getId(), name.getName());
			descriptions.put(name.getLang().getId(), name.getDescription());
		}

		for (Language lang : ApplicationConfig.getInstance().getLanguages()) {
			if (names.containsKey(lang.getId())) {
				continue;
			}
			names.put(lang.getId(), "");
			descriptions.put(lang.getId(), "");
		}
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

	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}
}
