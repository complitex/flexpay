package org.flexpay.common.actions.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NonNls;

import javax.servlet.http.HttpServletRequest;

public class MenuInterceptor extends AbstractInterceptor {

	@NonNls
	protected Logger log = LoggerFactory.getLogger(getClass());

	public String intercept(ActionInvocation actionInvocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		log.debug("Current action: request.getServletPath() = {}, request.getContextPath() = {}", request.getServletPath(), request.getContextPath());
		request.getSession().setAttribute("currentAction", request.getServletPath());
		return actionInvocation.invoke();
	}

}
