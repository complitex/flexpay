package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.persistence.AddressAttributeTypeTranslation;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class AddressAttributeTypeEditAction extends FPActionSupport {

	private AddressAttributeType attributeType = new AddressAttributeType();
	private Map<Long, String> names = treeMap();
	private Map<Long, String> shortNames = treeMap();

	private String crumbCreateKey;
	private AddressAttributeTypeService addressAttributeTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (attributeType == null || attributeType.getId() == null) {
			log.warn("Incorrect attribute type id");
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		}

		if (attributeType.isNotNew()) {
			Stub<AddressAttributeType> stub = stub(attributeType);
			attributeType = addressAttributeTypeService.readFull(stub);

			if (attributeType == null) {
				log.warn("Can't get address attribute type with id {} from DB", stub.getId());
				addActionError(getText("common.object_not_selected"));
				return REDIRECT_ERROR;
			} else if (attributeType.isNotActive()) {
				log.warn("Attribute type with id {} is disabled", stub.getId());
				addActionError(getText("common.object_not_selected"));
				return REDIRECT_ERROR;
			}

		}

//		AddressAttributeType type = attributeType;

		if (names == null) {
			log.debug("Names parameter is null");
			names = treeMap();
		}

		if (shortNames == null) {
			log.debug("ShortNames parameter is null");
			shortNames = treeMap();
		}

		if (isNotSubmit()) {
			initData();
			return INPUT;
		}

		// init translations
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			String shortName = shortNames.get(name.getKey());
			AddressAttributeTypeTranslation translation = new AddressAttributeTypeTranslation(value, shortName, lang);
			attributeType.setTranslation(translation);
		}

		if (attributeType.isNew()) {
			addressAttributeTypeService.create(attributeType);
		} else {
			addressAttributeTypeService.update(attributeType);
		}

		addActionMessage(getText("ab.buildings_attribute_type.saved"));

		return REDIRECT_SUCCESS;
	}

	private void initData() {

		for (AddressAttributeTypeTranslation translation : attributeType.getTranslations()) {
			names.put(translation.getLang().getId(), translation.getName());
			shortNames.put(translation.getLang().getId(), translation.getShortName());
		}

		for (Language language : getLanguages()) {
			if (!names.containsKey(language.getId())) {
				names.put(language.getId(), "");
				shortNames.put(language.getId(), "");
			}
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
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	@Override
	protected void setBreadCrumbs() {
		if (attributeType != null && attributeType.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public AddressAttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(AddressAttributeType attributeType) {
		this.attributeType = attributeType;
	}

	public Map<Long, String> getNames() {
		return names;
	}

	public void setNames(Map<Long, String> names) {
		this.names = names;
	}

	public Map<Long, String> getShortNames() {
		return shortNames;
	}

	public void setShortNames(Map<Long, String> shortNames) {
		this.shortNames = shortNames;
	}

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setBuildingAttributeTypeService(AddressAttributeTypeService addressAttributeTypeService) {
		this.addressAttributeTypeService = addressAttributeTypeService;
	}

}
