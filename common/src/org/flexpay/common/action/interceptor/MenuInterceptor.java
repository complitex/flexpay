package org.flexpay.common.action.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import net.sourceforge.navigator.menu.MenuComponent;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

public class MenuInterceptor extends AbstractInterceptor {

	protected Logger log = LoggerFactory.getLogger(getClass());

    @Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {

		log.debug("Menu interceptor");

		HttpServletRequest request = ServletActionContext.getRequest();

		String menu = request.getParameter("menu");

		String activeMenu = (String) WebUtils.getSessionAttribute(request, MenuComponent.ACTIVE_MENU);
		if (StringUtils.isNotEmpty(menu) && !menu.equals(activeMenu)) {
			WebUtils.setSessionAttribute(request, MenuComponent.ACTIVE_MENU, menu);
		}

		WebUtils.setSessionAttribute(request, "currentAction", request.getServletPath());

		return actionInvocation.invoke();
	}

}
