package org.flexpay.common.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.config.Namespace;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.common.actions.breadcrumbs.Crumb;
import org.flexpay.common.actions.interceptor.BreadCrumbAware;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.util.*;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Helper ActionSupport extension, able to set
 */
@SuppressWarnings ({"UnusedDeclaration"})
@Namespace ("")
public abstract class FPActionSupport extends ActionSupport implements BreadCrumbAware, SessionAware {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private static final String ERRORS_SESSION_ATTRIBUTE = FPActionSupport.class.getName() + ".ERRORS";
	private static final String MESSAGES_SESSION_ATTRIBUTE = FPActionSupport.class.getName() + ".MESSAGES";
	protected static final String PREFIX_REDIRECT = "redirect";

	public static final String REDIRECT_ERROR = "redirectError";
	public static final String REDIRECT_SUCCESS = "redirectSuccess";
	public static final String REDIRECT_INPUT = "redirectInput";
	public static final String FILE = "file";

	private String WILDCARD_SEPARATOR = "!";

	private static final String METHOD_POST = "post";

	protected Crumb crumb;
	@SuppressWarnings ({"RawUseOfParameterizedType"})
	protected Map session = CollectionUtils.map();
	protected String submitted;
	protected String menu;
	protected String crumbNameKey;

	public boolean isSubmit() {
		return submitted != null;
	}

	public boolean isNotSubmit() {
		return !isSubmit();
	}

	/**
	 * @return Execution result
	 * @throws Exception if failure occurs
	 */
	@SuppressWarnings ({"RawUseOfParameterizedType"})
	@Override
	public String execute() throws Exception {

		String result;
		try {
			setBreadCrumbs();
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

		log.debug("Current errors: {}", getActionErrors());
		log.debug("Current messages: {}", getActionMessages());

		addSessionMessages(result, ERRORS_SESSION_ATTRIBUTE);
		addSessionMessages(result, MESSAGES_SESSION_ATTRIBUTE);

		return result;
	}

	@SuppressWarnings ({"unchecked"})
	private void addSessionMessages(String result, String sessionAttribute) {

		boolean isError = false;

		if (ERRORS_SESSION_ATTRIBUTE.equals(sessionAttribute)) {
			isError = true;
		} else if (!MESSAGES_SESSION_ATTRIBUTE.equals(sessionAttribute)) {
			return;
		}

		// extract this domain session messages
		String domainName = getDomainName();

		Map<String, Collection<String>> domainNamesToMessages = (Map) session.remove(sessionAttribute);
		if (domainNamesToMessages != null && domainNamesToMessages.containsKey(domainName)) {
			Collection<String> messages = domainNamesToMessages.remove(domainName);
			if (isError) {
				addActionErrors(messages);
			} else {
				addActionMessages(messages);
			}

			if (log.isDebugEnabled()) {
				log.debug("Added session {}: {}", (isError ? "errors" : "messages"), messages);
			}
		}

		// put all messages to session if redirecting
		if (result.startsWith(PREFIX_REDIRECT)) {
			Collection<String> messages = isError ? getActionErrors() : getActionMessages();
			if (domainNamesToMessages == null) {
				domainNamesToMessages = CollectionUtils.map();
			}
			domainNamesToMessages.put(getDomainName(), messages);
		}

		session.put(sessionAttribute, domainNamesToMessages);
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

	protected void setBreadCrumbs() {
		if (getUserPreferences() == null || crumb == null) {
			log.debug("No user preferences found or crumb. Action execution terminated.");
			return;
		}

		if (crumbNameKey == null) {
			log.debug("For this action breadcrumb name not initialize.");
			return;
		}

		Stack<Crumb> crumbs = getUserPreferences().getCrumbs();
		if (crumbs == null) {
			crumbs = new Stack<Crumb>();
		}

		Object menuObj = crumb.getRequestParams().get("menu");
		String menuParam = null;
		if (menuObj != null) {
			menuParam = ((String[]) menuObj)[0];
		}
		if (menuParam != null && crumb.getRequestParams().size() == 1) {
			deleteOldCrumbs(0);
		}

		String qualifiedActionName = crumb.getNameSpace() + "/" + crumb.getAction();
		Crumb crumbToReplace = findCrumbWithCurAction(qualifiedActionName);
		if (crumbToReplace != null) {
			deleteOldCrumbs(getUserPreferences().getCrumbs().indexOf(crumbToReplace));
		}
		crumb.setWildPortionOfName(crumbNameKey);

		crumbs.add(crumb);
	}

	protected void deleteOldCrumbs(int index) {
		int size = getUserPreferences().getCrumbs().size();
		for (int i = index; i < size; i++) {
			getUserPreferences().getCrumbs().remove(index);
		}
	}

	protected Crumb findCrumbWithCurAction(String qualifiedActionName) {
		for (Crumb crumb : getUserPreferences().getCrumbs()) {
			if (crumb.getQualifiedActionName().equals(qualifiedActionName)) {
				return crumb;
			}
		}
		return null;
	}

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
	 * Add several action messages
	 *
	 * @param messages Collection of messages
	 */
	@SuppressWarnings ({"unchecked"})
	public void addActionMessages(Collection<String> messages) {

		if (messages == null || messages.isEmpty()) {
			return;
		}

		for (String msg : messages) {
			addActionMessage(msg);
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
			log.debug("Adding error: {}, params: {}", e.getErrorKey(), StringUtils.join(e.getParams(), ","));
			try {
				return getText(e.getErrorKey(), e.getParams());
			} catch (RuntimeException ex) {
				log.error("Failed getting text " + e.getErrorKey() + " with parameters " +
						  Arrays.asList(e.getParams()), ex);
				return e.getMessage();
			}
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

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public Crumb getCrumb() {
		return crumb;
	}

	@Override
	public void setCrumb(Crumb crumb) {
		this.crumb = crumb;
	}

	public UserPreferences getUserPreferences() {
		return (UserPreferences) SecurityUtil.getAuthentication().getPrincipal();
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
		return LanguageUtil.getLanguageName(lang, getUserPreferences().getLocale())
				.getTranslation();
	}

	public <T extends Translation> T getTranslation(Set<T> translations) {
		return TranslationUtil.getTranslation(translations, getUserPreferences().getLocale());
	}

	public <T extends Translation> String getTranslationName(Set<T> translations) {
		return getTranslation(translations).getName();
	}

	public String format(Date date) {
		String dt = DateUtil.format(date);
		return "-".equals(dt) ? "" : dt;
	}

    public String formatWithTime(Date date) {
        String dt = DateUtil.formatWithTime(date);
        return "-".equals(dt) ? "" : dt;
    }

	/**
	 * Sets the Map of session attributes in the implementing class.
	 *
	 * @param session a Map of HTTP session attribute name/value pairs.
	 */
	@SuppressWarnings ({"RawUseOfParameterizedType"})
	@Override
	public void setSession(Map session) {
		this.session = session;
	}

	protected Language getLanguage() {
		Locale locale = getUserPreferences().getLocale();
		return LanguageUtil.getLanguage(locale);
	}

    public void setCrumbNameKey(String crumbNameKey) {
        this.crumbNameKey = crumbNameKey;
    }
}
