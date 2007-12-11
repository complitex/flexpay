package org.flexpay.ab.actions;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.config.UserPreferences;

public class StreetTypesList extends FPActionSupport implements ServletRequestAware, SessionAware {

	private static Logger log = Logger.getLogger(TownTypesList.class);

	private static final String ATTRIBUTE_ACTION_ERRORS =
			StreetTypesList.class.getName() + ".ACTION_ERRORS";

	private HttpServletRequest request;
	private Map session;
	private StreetTypeService streetTypeService;

	@SuppressWarnings ({"unchecked"})
	public String execute() {
		try {
			UserPreferences prefs = UserPreferences.getPreferences(request);
			List<StreetTypeTranslation> translations =
					streetTypeService.getStreetTypeTranslations(prefs.getLocale());
			request.setAttribute("street_type_names", translations);
		} catch (FlexPayException e) {
			addActionError(e);
		}

		// Retrive action errors from session if any
		if (log.isDebugEnabled()) {
			log.debug("Getting actionErrors: " + session.get(ATTRIBUTE_ACTION_ERRORS));
		}
		Collection errors = (Collection) session.remove(ATTRIBUTE_ACTION_ERRORS);
		if (errors != null && !errors.isEmpty()) {
			Collection actionErrors = getActionErrors();
			actionErrors.addAll(errors);
			setActionErrors(actionErrors);
		}

		return SUCCESS;
	}

	public static void setActionErrors(Map<String, Object> session, Collection actionErrors) {
		if (log.isDebugEnabled()) {
			log.debug("Setting actionErrors: " + actionErrors);
		}
		session.put(ATTRIBUTE_ACTION_ERRORS, actionErrors);
	}

	/**
	 * Setter for property 'streetTypeService'.
	 *
	 * @param streetTypeService Value to set for property 'townTypeService'.
	 */
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
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
	 * Sets the Map of session attributes in the implementing class.
	 *
	 * @param session a Map of HTTP session attribute name/value pairs.
	 */
	public void setSession(Map session) {
		this.session = session;
	}
}