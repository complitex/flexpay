package org.flexpay.common.actions.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RolesInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: vlad
 * Date: 21.11.2007
 * Time: 10:28:04
 */
public class FlexpayRolesInterceptor extends RolesInterceptor {

    private static final String DEFAULT_FORBIDDEN_URL = "/resources/common/jsp/forbidden.jsp";
    private static final String DEFAULT_LOGIN_URL = "/login.jsp";

    private String forbiddenURL = null;
    private String loginURL = null;

    public void setForbiddenURL(String forbiddenURL) {
        this.forbiddenURL = forbiddenURL;
    }

    public void setLoginURL(String loginURL) {
        this.loginURL = loginURL;
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String result = null;
        if (request.getRemoteUser() == null) {
            //redirectToLogin(invocation, request, response);
        } else if (!isAllowed(request, invocation.getAction())) {
            redirectToForbidden(invocation, request, response);
        } else {
            result = invocation.invoke();
        }
        return result;
    }

    /*protected void redirectToLogin(ActionInvocation invocation,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
            throws Exception {
        String lastRequestedURL = request.getRequestURI();
        if (lastRequestedURL != null && !"".equals(lastRequestedURL)) {
            request.getSession().setAttribute(LastRequestFilter.LAST_REQUESTED_URL_ATTR_NAME, lastRequestedURL);
        }
        String redirecrURL = request.getContextPath() + (loginURL == null || "".equals(loginURL) ? DEFAULT_LOGIN_URL : loginURL);
        response.sendRedirect(redirecrURL);
    }*/

    protected String redirectToForbidden(ActionInvocation invocation,
                                         HttpServletRequest request,
                                         HttpServletResponse response)
            throws Exception {
        response.sendRedirect(forbiddenURL == null || "".equals(forbiddenURL) ? request.getContextPath() + DEFAULT_FORBIDDEN_URL : request.getContextPath() + forbiddenURL); // response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return null;
    }
}
