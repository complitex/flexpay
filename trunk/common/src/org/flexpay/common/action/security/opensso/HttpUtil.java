package org.flexpay.common.action.security.opensso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;

public class HttpUtil {

    private static Logger log = LoggerFactory.getLogger(HttpUtil.class);

    public static void printCookies(HttpServletRequest request) {
        if (log.isDebugEnabled()) {
            Enumeration<?> headers = request.getHeaderNames();
            while (headers.hasMoreElements()) {
                String header = (String) headers.nextElement();
                log.debug("Header: {} = {}", header, request.getHeader(header));
            }

            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                log.debug("Cookies are null!");
                return;
            }
            if (cookies.length == 0) {
                log.debug("Cookies are empty!");
            } else {
                log.debug("Cookies.length: {}", cookies.length);
                for (Cookie cookie : cookies) {
                    String comment = cookie.getComment();
                    String domain = cookie.getDomain();
                    Integer maxAge = cookie.getMaxAge();
                    String name = cookie.getName();
                    String path = cookie.getPath();
                    Boolean secure = cookie.getSecure();
                    String value = cookie.getValue();
                    Integer version = cookie.getVersion();
                    log.debug("Cookie: name: {}, domain: {}, path: {}, value: {}, secure: {}, maxAge: {}, version: {}, comment {}",
                            new Object[] {name, domain, path, value, secure, maxAge, version, comment});
                }
            }
        }
    }

    public static HttpServletRequest unwrapOriginalHttpServletRequest(HttpServletRequest request) {
        if (request instanceof HttpServletRequestWrapper) {
            log.debug("Found HttpServletRequestWrapper: unwrapping..");
            HttpServletRequestWrapper wrapper = (HttpServletRequestWrapper) request;
            ServletRequest servletRequest = wrapper.getRequest();
            if (servletRequest instanceof HttpServletRequest) {
                log.debug("Unwrapped original HttpServletRequest");
                request = (HttpServletRequest) servletRequest;
            } else {
                log.debug("Unwrapped a " + servletRequest);
            }
        } else {
            log.debug("Found a " +  request);
        }
        return request;
    }
}
