package org.flexpay.common.test;

import static org.flexpay.common.service.Roles.*;
import org.flexpay.common.util.SecurityUtil;
import org.jetbrains.annotations.NonNls;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.io.InputStream;

/**
 * Base class for all SpringFramework initialised beans aware tests
 */
@ContextConfiguration (locations = {
		"file:WEB-INF/applicationContext.xml"
		, "applicationContext-security.xml"
		, "file:WEB-INF/common/configs/spring/history/beans-ws-server-test.xml"
})
public abstract class SpringBeanAwareTestCase extends AbstractJUnit4SpringContextTests {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private Scheduler scheduler;

	@Autowired
	@Qualifier ("hibernateTemplate")
	protected HibernateTemplate hibernateTemplate;
	@Autowired
	@Qualifier ("jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;

	protected InputStream getFileStream(@NonNls String relativePath) {
		return getClass().getClassLoader().getResourceAsStream(relativePath);
	}

	@Before
	public void stopScheduler() throws Exception {
		scheduler = (Scheduler) applicationContext.getBean("schedulerFactoryBean");
		if (scheduler != null) {
			scheduler.shutdown();
		}
	}

	/**
	 * Authenticate test user
	 */
	@BeforeClass
	public static void authenticateTestUser() {
		GrantedAuthority[] authorities = SecurityUtil.auths(
				BASIC,
				PROCESS_DEFINITION_UPLOAD_NEW,
				PROCESS_READ
		);
		User user = new User("test", "test", true, true, true, true, authorities);
		Authentication auth = new AnonymousAuthenticationToken("key", user, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
