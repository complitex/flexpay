package org.flexpay.ab.action.identity;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.IdentityTypeTranslation;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class IdentityTypeEditAction extends FPActionSupport {

	private IdentityType identityType = new IdentityType();
	private Map<Long, String> names = treeMap();

	private String crumbCreateKey;
	private IdentityTypeService identityTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (identityType == null || identityType.getId() == null) {
			log.warn("Identity type id not set");
			addActionError(getText("ab.error.identity_type.incorrect_identity_type_id"));
			return REDIRECT_ERROR;
		}

		if (identityType.isNotNew()) {
			Stub<IdentityType> stub = stub(identityType);
			identityType = identityTypeService.readFull(stub);

			if (identityType == null) {
				log.warn("Can't get identity type with id {} from DB", stub.getId());
				addActionError(getText("ab.error.identity_type.cant_get_identity_type"));
				return REDIRECT_ERROR;
			} else if (identityType.isNotActive()) {
				log.warn("Identity type with id {} is disabled", stub.getId());
				addActionError(getText("ab.error.identity_type.cant_get_identity_type"));
				return REDIRECT_ERROR;
			}

		}

		correctNames();

		if (isNotSubmit()) {
			initData();
			return INPUT;
		}

		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			identityType.setTranslation(new IdentityTypeTranslation(value, lang));
		}

		if (identityType.isNew()) {
			identityTypeService.create(identityType);
		} else {
			identityTypeService.update(identityType);
		}

		addActionMessage(getText("ab.identity_type.saved"));

		return REDIRECT_SUCCESS;
	}

	private void correctNames() {
		if (names == null) {
			log.warn("Names parameter is null");
			names = treeMap();
		}
		Map<Long, String> newNames = treeMap();
		for (Language lang : getLanguages()) {
			newNames.put(lang.getId(), names.containsKey(lang.getId()) ? names.get(lang.getId()) : "");
		}
		names = newNames;
	}

	private void initData() {

		for (IdentityTypeTranslation translation : identityType.getTranslations()) {
			names.put(translation.getLang().getId(), translation.getName());
		}

		for (Language language : getLanguages()) {
			if (!names.containsKey(language.getId())) {
				names.put(language.getId(), "");
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
		if (identityType != null && identityType.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public IdentityType getIdentityType() {
		return identityType;
	}

	public void setIdentityType(IdentityType identityType) {
		this.identityType = identityType;
	}

	public Map<Long, String> getNames() {
		return names;
	}

	public void setNames(Map<Long, String> names) {
		this.names = names;
	}

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}

}
