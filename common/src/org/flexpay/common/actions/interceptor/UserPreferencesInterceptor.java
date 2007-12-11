package org.flexpay.common.actions.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.flexpay.common.util.config.UserPreferences;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class UserPreferencesInterceptor extends AbstractInterceptor {

	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		UserPreferences userPreferences = UserPreferences
				.getPreferences(request);
		UserPreferencesAware action = (UserPreferencesAware) invocation
				.getAction();
		action.setUserPreferences(userPreferences);

		return invocation.invoke();
	}

}
