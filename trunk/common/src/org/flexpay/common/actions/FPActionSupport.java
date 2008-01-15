package org.flexpay.common.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.common.actions.interceptor.UserPreferencesAware;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * Helper ActionSupport extension, able to set
 */
public class FPActionSupport extends ActionSupport implements UserPreferencesAware {

	private static Logger log = Logger.getLogger(FPActionSupport.class);

	protected UserPreferences userPreferences;

	/**
	 * Add several action errors
	 *
	 * @param errorMessages Collection of error messages
	 */
	@SuppressWarnings ({"unchecked"})
	public void addActionErrors(Collection<String> errorMessages) {
		Collection<String> actionErrors = getActionErrors();
		actionErrors.addAll(errorMessages);
		setActionErrors(actionErrors);
	}

	/**
	 * Translate FlexPayException to action error
	 *
	 * @param e FlexPayException
	 */
	public void addActionError(FlexPayException e) {
		addActionError(getText(e.getErrorKey(), e.getParams()));
	}

	/**
	 * Translate several FlexPayExceptions to action errors
	 *
	 * @param container FlexPayExceptionContainer
	 */
	public void addActionErrors(FlexPayExceptionContainer container) {
		for (FlexPayException e : container.getExceptions()) {
			addActionError(e);
		}
	}

	public static Date getDateParam(HttpServletRequest request, String name)
			throws FlexPayException {

		String param = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			param = request.getParameter(name);
			Date dt = df.parse(param);

			if (log.isDebugEnabled()) {
				log.debug("Parsed date: " + name + ": " + dt);
			}

			return dt;
		} catch (Exception e) {
			if (StringUtils.isNotBlank(param)) {
				throw new FlexPayException("Invalid date", "error.invalid_date", param);
			}
			if (log.isDebugEnabled()) {
				log.debug("Cannot parse date " + name + ": " + param);
			}
			return null;
		}
	}

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public String getLangName(Language lang) throws FlexPayException {
		return LanguageUtil.getLanguageName(lang, userPreferences.getLocale())
				.getTranslation();
	}

	public Translation getTranslation(Set<Translation> translations) throws FlexPayException {
		return TranslationUtil.getTranslation(translations, userPreferences.getLocale());
	}

	public boolean isPost() {
		return "post".equalsIgnoreCase(ServletActionContext.getRequest().getMethod());
	}

	public String format(Date date) {
		String dt = DateIntervalUtil.format(date);
		return "-".equals(dt) ? "" : dt;
	}
}
