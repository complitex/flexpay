package org.flexpay.ab.actions;

import com.opensymphony.xwork2.Preparable;
import org.flexpay.ab.service.MultilangEntityService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.actions.FPActionSupport;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCreateAction<Entity, T extends Translation>
		extends FPActionSupport implements Preparable {

	private List<T> translationList;

	protected abstract T createTranslation();

	protected abstract MultilangEntityService<Entity, T> getEntityService();

	public void prepare() throws FlexPayException {
		List<Language> languageList = ApplicationConfig.getLanguages();
		translationList = new ArrayList<T>(languageList.size());
		for (Language lang : languageList) {
			T translation = createTranslation();
			translation.setLang(lang);
			translationList.add(translation);
		}
	}

	public String doExecute() throws Exception {

		if (isSubmit()) {
			getEntityService().create(translationList);
			return "afterSubmit";
		}

		return "form";
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * TODO: introduce constant
	 *
	 * @return {@link #ERROR} by default
	 */
	@Override
	protected String getErrorResult() {
		return "afterSubmit";
	}

	public List<T> getTranslationList() {
		return translationList;
	}
}
