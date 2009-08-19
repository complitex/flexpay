package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.persistence.AddressAttributeTypeTranslation;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;
import java.util.Set;

public class AddressAttributeTypeEditAction extends FPActionSupport {

	private AddressAttributeType attributeType = new AddressAttributeType();
	private Map<Long, String> names = treeMap();
	private Map<Long, String> shortNames = treeMap();

	private String crumbCreateKey;
	private AddressAttributeTypeService addressAttributeTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (attributeType.getId() == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		AddressAttributeType type = attributeType.isNew() ?
									attributeType :
									addressAttributeTypeService.read(stub(attributeType));
		if (type == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		if (isNotSubmit()) {
			attributeType = type;
			initTranslations();
			return INPUT;
		}

		// init translations
		Set<AddressAttributeTypeTranslation> newTranslations = CollectionUtils.set();
		for (Long languageId : names.keySet()) {
			Language lang = getLang(languageId);
			String name = names.get(languageId);
			String shortName = shortNames.get(languageId);
			AddressAttributeTypeTranslation translation = type.getTranslation(lang);
			if (translation != null) {
				translation.setName(name);
				translation.setShortName(shortName);
			} else {
				translation = new AddressAttributeTypeTranslation(name, shortName, lang);
				type.setTranslation(translation);
			}
		}

		if (type.isNew()) {
			addressAttributeTypeService.create(type);
		} else {
			addressAttributeTypeService.update(type);
		}

		return REDIRECT_SUCCESS;
	}

	private void initTranslations() {

		for (AddressAttributeTypeTranslation translation : attributeType.getTranslations()) {
			names.put(translation.getLang().getId(), translation.getName());
			shortNames.put(translation.getLang().getId(), translation.getShortName());
		}

		for (Language language : ApplicationConfig.getLanguages()) {
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
		if (attributeType.isNew()) {
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
