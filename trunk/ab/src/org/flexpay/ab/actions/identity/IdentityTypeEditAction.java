package org.flexpay.ab.actions.identity;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.IdentityTypeTranslation;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
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

		identityType = identityType.isNew() ? identityType : identityTypeService.readFull(stub(identityType));

		if (identityType == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		if (isNotSubmit()) {
			initTranslations();
			return INPUT;
		}

		// init translations
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

	private void initTranslations() {

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
		if (identityType.isNew()) {
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
