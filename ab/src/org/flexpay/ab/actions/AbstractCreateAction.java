package org.flexpay.ab.actions;

import com.opensymphony.xwork2.Preparable;
import org.flexpay.ab.service.MultilangEntityService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCreateAction<Entity, T extends Translation>
		extends CommonAction implements Preparable {

	private List<T> translationList;

	protected abstract T createTranslation();

	protected abstract MultilangEntityService<Entity, T> getEntityService();

	public void prepare() throws FlexPayException {
		List<Language> languageList = ApplicationConfig.getInstance()
				.getLanguages();
		translationList = new ArrayList<T>(languageList.size());
		for (Language lang : languageList) {
			T translation = createTranslation();
			translation.setLang(lang);
			translationList.add(translation);
		}
	}

	public String execute() throws Exception {

		if (isSubmitted()) {
			try {
				getEntityService().create(translationList);
			} catch (FlexPayException e) {
				// TODO
			}
			return "afterSubmit";
		}

		return "form";
	}

	public List<T> getTranslationList() {
		return translationList;
	}
}
