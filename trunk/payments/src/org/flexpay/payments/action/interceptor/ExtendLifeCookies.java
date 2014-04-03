package org.flexpay.payments.action.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.CreateSessionInterceptor;
import org.flexpay.payments.action.security.SetCashboxIdAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Pavel Sknar
 */
public class ExtendLifeCookies extends CreateSessionInterceptor {

    private Logger log = LoggerFactory.getLogger(getClass());

    public static final int MOUNTH_IN_SECONDS = 2628000;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {

        return super.intercept(invocation);
    }
}
