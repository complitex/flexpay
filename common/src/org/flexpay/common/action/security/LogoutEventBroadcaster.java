package org.flexpay.common.action.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a logout handler that publishes a LogoutEvent when a logout occurs. With this handler in place, one need only
 * create a listener to listen for logout events in order to do some customer processing at logout.
 * <p/>
 * See <a href="http://forum.springsource.org/showthread.php?t=29115">http://forum.springsource.org/showthread.php?t=29115</a> for details
 *
 * @author pea1
 */
public class LogoutEventBroadcaster implements LogoutHandler, ApplicationContextAware {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ApplicationContext applicationContext;

	/**
	 *
	 */
	public LogoutEventBroadcaster() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.acegisecurity.ui.logout.LogoutHandler#logout(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.acegisecurity.Authentication)
	 */
    @Override
	public void logout(HttpServletRequest arg0, HttpServletResponse arg1, Authentication auth) {
		if (auth == null) {
			log.debug("No authentication on log out, had session ended before log out was issued?");
			return;
		}
		LogoutEvent event = new LogoutEvent(auth);
		log.debug("publishing logout event: {}", event);
		applicationContext.publishEvent(event);
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
    @Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
