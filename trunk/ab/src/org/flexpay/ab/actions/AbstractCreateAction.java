package org.flexpay.ab.actions;

import java.util.ArrayList;
import java.util.List;

import org.flexpay.ab.persistence.AbstractTranslation;
import org.flexpay.ab.service.MultilangEntityService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.config.ApplicationConfig;

import com.opensymphony.xwork2.Preparable;

public abstract class AbstractCreateAction<Entity, Translation extends AbstractTranslation>
		extends CommonAction implements Preparable {

	private List<Translation> translationList;

	protected abstract Translation createTranslation();

	protected abstract MultilangEntityService<Entity, Translation> getEntityService();

	public void prepare() throws FlexPayException {
		List<Language> languageList = ApplicationConfig.getInstance()
				.getLanguages();
		translationList = new ArrayList<Translation>(languageList.size());
		for (Language lang : languageList) {
			Translation translation = createTranslation();
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

	public List<Translation> getTranslationList() {
		return translationList;
	}
}
