package org.flexpay.ab.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.config.UserPreferences;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class TownTypesList implements ServletRequestAware {

	private HttpServletRequest request;
	private TownTypeService townTypeService;

	public String execute() throws FlexPayException {
		UserPreferences prefs = UserPreferences.getPreferences(request);
		List<TownTypeTranslation> translations =
				townTypeService.getTownTypeTranslations(prefs.getLocale());
		request.setAttribute("town_type_names", translations);

		return ActionSupport.SUCCESS;
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
	 * Sets the HTTP request object in implementing classes.
	 *
	 * @param request the HTTP request.
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
