package org.flexpay.ab.actions.identity;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.IdentityTypeTranslation;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class IdentityTypeEditAction extends FPActionSupport {

	private IdentityTypeService identityTypeService;

	private IdentityType identityType = new IdentityType();
	private Map<Long, String> langToTranslation = treeMap();

	@NotNull
	public String doExecute() throws Exception {
		if (identityType.isNew()) {
			throw new FlexPayException("No id specified");
		}

		identityType = identityTypeService.read(identityType.getId());
		if (isSubmit()) {
			for (Long langId : langToTranslation.keySet()) {
				Language lang = getLang(langId);
				IdentityTypeTranslation translation = new IdentityTypeTranslation();
				translation.setLang(lang);
				translation.setName(langToTranslation.get(langId));
				identityType.setTranslation(translation);
			}
			if (log.isDebugEnabled()) {
				log.debug("Type translations: " + identityType.getTranslations());
			}
			identityTypeService.save(identityType);

			return REDIRECT_SUCCESS;
		} else {
			initTranslations();
		}

		return INPUT;
	}

	private void initTranslations() {

		for (IdentityTypeTranslation translation : identityType.getTranslations()) {
			langToTranslation.put(translation.getLang().getId(), translation.getName());
		}

		for (Language language : ApplicationConfig.getLanguages()) {
			if (!langToTranslation.containsKey(language.getId())) {
				langToTranslation.put(language.getId(), "");
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

	public Map<Long, String> getLangToTranslation() {
		return langToTranslation;
	}

	public void setLangToTranslation(Map<Long, String> langToTranslation) {
		this.langToTranslation = langToTranslation;
	}

	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}
}
