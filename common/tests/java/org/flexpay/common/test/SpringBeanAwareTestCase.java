package org.flexpay.common.test;

import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.junit.Before;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.service.Roles.*;

/**
 * Base class for all SpringFramework initialised beans aware tests
 */
@ContextConfiguration (locations = {
		"file:WEB-INF/applicationContext.xml"
		, "applicationContext-security.xml"
        , "beans.xml"
		, "file:WEB-INF/common/configs/spring/history/beans-ws-server-test.xml"
})
public abstract class SpringBeanAwareTestCase extends AbstractJUnit4SpringContextTests {

	protected Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier ("hibernateTemplate")
	protected HibernateTemplate hibernateTemplate;
	@Autowired
	@Qualifier ("jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;

	protected InputStream getFileStream(String relativePath) {
		return getClass().getClassLoader().getResourceAsStream(relativePath);
	}

	@Before
	public void stopScheduler() throws Exception {
		Map<?, ?> schedulers = applicationContext.getBeansOfType(Scheduler.class);
		for (Object obj : schedulers.values()) {
			Scheduler scheduler = (Scheduler) obj;
			if (scheduler != null) {
				scheduler.shutdown();
			}
		}
	}

	/**
	 * Authenticate test user
	 */
	@Before
	public void authenticateTestUser() {
		List<GrantedAuthority> authorities = SecurityUtil.auths(
				BASIC,
				PROCESS_DEFINITION_UPLOAD_NEW,
				PROCESS_READ,
				PROCESS_DELETE
		);
		User user = new User("test", "test", true, true, true, true, authorities);
		UserPreferences preferences = new UserPreferences();
		preferences.setTargetDetails(user);
		Authentication auth = new AnonymousAuthenticationToken("key", preferences, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
