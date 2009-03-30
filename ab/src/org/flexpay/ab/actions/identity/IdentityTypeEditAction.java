package org.flexpay.ab.actions.identity;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.IdentityTypeTranslation;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.jetbrains.annotations.NotNull;
import org.hibernate.Hibernate;

import java.util.Map;

public class IdentityTypeEditAction extends FPActionSupport {

	private IdentityTypeService identityTypeService;

	private IdentityType identityType = new IdentityType();
	private Map<Long, String> names = treeMap();

	@NotNull
	public String doExecute() throws Exception {

		if (identityType.getId() == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		IdentityType type = identityType.isNew() ? identityType : identityTypeService.read(identityType.getId());

		if (!isSubmit()) {
			identityType = type;            
			initTranslations();
			return INPUT;
		}

		// init translations
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			IdentityTypeTranslation translation = new IdentityTypeTranslation();
			translation.setLang(lang);
			translation.setName(value);
			type.setTranslation(translation);
		}

		if (type.isNew()) {
			identityTypeService.create(type);
		} else {
			identityTypeService.update(type);
		}

		return REDIRECT_SUCCESS;
	}

	private void initTranslations() {

		for (IdentityTypeTranslation translation : identityType.getTranslations()) {
			names.put(translation.getLang().getId(), translation.getName());
		}

		for (Language language : ApplicationConfig.getLanguages()) {
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

	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}
}
