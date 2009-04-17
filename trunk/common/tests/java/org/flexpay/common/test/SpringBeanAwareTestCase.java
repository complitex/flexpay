package org.flexpay.common.test;

import org.flexpay.common.service.Roles;
import org.jetbrains.annotations.NonNls;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.quartz.Scheduler;

import java.io.InputStream;
import java.util.Map;

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

	@Test
	public void testSchedulerDisabled() throws Throwable {
		assertTrue("Scheduler was not disabled", scheduler == null || scheduler.isShutdown());
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
		GrantedAuthority[] BASIC_AUTHORITIES = {new GrantedAuthorityImpl(Roles.BASIC)};
		User user = new User("test", "test", true, true, true, true, BASIC_AUTHORITIES);
		Authentication auth = new AnonymousAuthenticationToken("key", user, BASIC_AUTHORITIES);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
