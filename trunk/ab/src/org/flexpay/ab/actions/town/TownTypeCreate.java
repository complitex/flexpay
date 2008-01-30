package org.flexpay.ab.actions.town;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.config.ApplicationConfig;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TownTypeCreate extends FPActionSupport implements ServletRequestAware {

	private HttpServletRequest request;
	private TownTypeService townTypeService;

	private Collection<TownTypeTranslation> townNames;

	public String execute() throws Exception {
		townNames = initTypeTranslations();

		// Need to create new TownType
		if (isPost()) {
			try {
				townTypeService.create(townNames);
				return ActionSupport.SUCCESS;
			} catch (FlexPayException e) {
				addActionError(e);
			}
		}

		return ActionSupport.INPUT;
	}

	private List<TownTypeTranslation> initTypeTranslations() throws Exception {
		List<Language> langs = ApplicationConfig.getInstance().getLanguages();
		List<TownTypeTranslation> translations = new ArrayList<TownTypeTranslation>(langs.size());

		for (Language lang : langs) {
			TownTypeTranslation translation = new TownTypeTranslation();
			translation.setLang(lang);

			// Actually got a form, extract data
			if (isPost()) {
				translation.setName(request.getParameter("name_" + lang.getId()));
			}

			translations.add(translation);
		}

		return translations;

	}

	/**
	 * Sets the HTTP request object in implementing classes.
	 *
	 * @param request the HTTP request.
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Setter for property 'townTypeService'.
	 *
	 * @param townTypeService Value to set for property 'townTypeService'.
	 */
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

	/**
	 * Getter for property 'townNames'.
	 *
	 * @return Value for property 'townNames'.
	 */
	public Collection<TownTypeTranslation> getTownNames() {
		return townNames;
	}
}
