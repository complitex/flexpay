package org.flexpay.common.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.common.actions.interceptor.UserPreferencesAware;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.util.*;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;

import java.util.*;

/**
 * Helper ActionSupport extension, able to set
 */
@SuppressWarnings ({"UnusedDeclaration"})
public abstract class FPActionSupport extends ActionSupport implements UserPreferencesAware, SessionAware {

	@NonNls
	protected Logger log = Logger.getLogger(getClass());

	@NonNls
	private static final String ERRORS_SESSION_ATTRIBUTE = FPActionSupport.class.getName() + ".ERRORS";
	@NonNls
	protected static final String PREFIX_REDIRECT = "redirect";

	@NonNls
	protected static final String REDIRECT_ERROR = "redirectError";
	@NonNls
	protected static final String REDIRECT_SUCCESS = "redirectSuccess";

	@NonNls
	private static final String METHOD_POST = "post";


	protected UserPreferences userPreferences;
	protected Map session = CollectionUtils.map();
	protected String submitted;

	public boolean isSubmit() {
		return submitted != null;
	}

	/**
	 * @return Execution result
	 * @throws Exception if failure occurs
	 */
	public final String execute() throws Exception {
		String result;
		try {
			result = doExecute();
		} catch (FlexPayException e) {
			addActionError(e);
			result = getErrorResult();
		} catch (FlexPayExceptionContainer e) {
			addActionErrors(e);
			result = getErrorResult();
		} catch (Exception e) {
			log.error("Unknown error", e);
			throw e;
		}

		if (log.isDebugEnabled()) {
			log.debug("Current errors: " + getActionErrors());
		}

		// extract this domain session errors
		String damainName = getDomainName();

		//noinspection unchecked
		Map<String, Collection> domainNamesToErrors = (Map) session.remove(ERRORS_SESSION_ATTRIBUTE);
		if (domainNamesToErrors != null && domainNamesToErrors.containsKey(damainName)) {
			Collection errors = domainNamesToErrors.remove(damainName);
			//noinspection unchecked
			addActionErrors(errors);

			if (log.isDebugEnabled()) {
				log.debug("Added errors: " + errors);
			}
		}

		// put all errors to session if redirecting
		if (result.startsWith(PREFIX_REDIRECT)) {
			Collection errors = getActionErrors();
			if (domainNamesToErrors == null) {
				domainNamesToErrors = CollectionUtils.map();
			}
			domainNamesToErrors.put(getDomainName(), errors);
		}
		//noinspection unchecked
		session.put(ERRORS_SESSION_ATTRIBUTE, domainNamesToErrors);

		return result;
	}

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	protected abstract String doExecute() throws Exception;

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected abstract String getErrorResult();

	/**
	 * Get domain name for error messages store, the default implementation returns an action package name
	 *
	 * @return Domain name
	 */
	protected String getDomainName() {
		return getClass().getPackage().getName();
	}

	/**
	 * Add several action errors
	 *
	 * @param errorMessages Collection of error messages
	 */
	@SuppressWarnings ({"unchecked"})
	public void addActionErrors(Collection<String> errorMessages) {

		if (errorMessages == null || errorMessages.isEmpty()) {
			return;
		}

		for (String msg : errorMessages) {
			addActionError(msg);
		}
	}

	/**
	 * Translate FlexPayException to action error
	 *
	 * @param e FlexPayException
	 */
	public void addActionError(@NotNull FlexPayException e) {
		addActionError(getErrorMessage(e));
	}

	@NotNull
	public String getErrorMessage(@NotNull FlexPayException e) {
		if (StringUtils.isNotEmpty(e.getErrorKey())) {
			return getText(e.getErrorKey(), e.getParams());
		} else {
			return e.getMessage();
		}
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

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	public void setSubmitted(String submitted) {
		this.submitted = submitted;
	}

	public Language getLang(@NotNull Long id) {
		for (Language lang : ApplicationConfig.getLanguages()) {
			if (id.equals(lang.getId())) {
				return lang;
			}
		}

		return null;
	}

	public String getLangName(Language lang) throws FlexPayException {
		return LanguageUtil.getLanguageName(lang, userPreferences.getLocale())
				.getTranslation();
	}

	public <T extends Translation> T getTranslation(Set<T> translations) throws FlexPayException {
		return TranslationUtil.getTranslation(translations, userPreferences.getLocale());
	}

	/**
	 * Check if request method was POST
	 *
	 * @return <code>true</code> if method was POST, or <code>false</code> otherwise
	 * @deprecated use {@link #isSubmit()} instead
	 */
	public boolean isPost() {
		return METHOD_POST.equalsIgnoreCase(ServletActionContext.getRequest().getMethod());
	}

	public String format(Date date) {
		String dt = DateUtil.format(date);
		return "-".equals(dt) ? "" : dt;
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
