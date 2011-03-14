package org.flexpay.common.action.interceptor;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.ServletActionContext;
import org.flexpay.common.action.breadcrumbs.Crumb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class BreadCrumbInterceptor extends AbstractInterceptor {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private String WILDCARD_SEPARATOR = "!";

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {

		log.debug("Bread crumbs interceptor");

		HttpServletRequest request = ServletActionContext.getRequest();

		try {

			Object action = actionInvocation.getAction();

			if (action instanceof BreadCrumbAware) {

				Crumb curCrumb = getCurrentCrumb(request);
				((BreadCrumbAware) action).setCrumb(curCrumb);
			}

			return actionInvocation.invoke();

		} catch (Exception e) {
			log.error("Failure", e);
			throw e;
		} catch (Throwable t) {
			log.error("Failure", t);
			throw new RuntimeException(t);
		}

	}

	private Crumb getCurrentCrumb(HttpServletRequest request) {

		ActionProxy proxy = ActionContext.getContext().getActionInvocation().getProxy();
		String actionName = proxy.getActionName();
		String nameSpace = proxy.getNamespace();
		String wildPortionOfName = actionName.substring(actionName.indexOf(WILDCARD_SEPARATOR) + 1);

		Crumb curCrumb = new Crumb(actionName, nameSpace, wildPortionOfName);
		curCrumb.setRequestParams(request.getParameterMap());

		return curCrumb;

	}

}
