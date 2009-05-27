package org.flexpay.common.actions.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.ServletActionContext;
import org.flexpay.common.util.config.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class UserPreferencesInterceptor extends AbstractInterceptor {

	private static final Logger log = LoggerFactory.getLogger(UserPreferencesInterceptor.class);

	public String intercept(ActionInvocation invocation) throws Exception {

		log.debug("User preferences interceptor");

		UserPreferences userPreferences = null;
		Object action;
		try {
			action = invocation.getAction();
			HttpServletRequest request = ServletActionContext.getRequest();
			if (action instanceof UserPreferencesAware) {
				userPreferences = UserPreferences.getPreferences(request);
				((UserPreferencesAware) action).setUserPreferences(userPreferences);
			}

			String result = invocation.invoke();

			if (action instanceof UserPreferencesAware) {
				UserPreferences.setPreferences(request, userPreferences);
			}

			log.debug("User preferences: {}", userPreferences);

			return result;
		} catch (Exception ex) {
			log.error("Failure", ex);
			throw ex;
		} catch (Throwable t) {
			log.error("Failure", t);
			throw new RuntimeException(t);
		}
	}

}
