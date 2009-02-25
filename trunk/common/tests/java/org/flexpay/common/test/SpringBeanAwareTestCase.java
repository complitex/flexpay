package org.flexpay.common.test;

import org.jetbrains.annotations.NonNls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.security.GrantedAuthority;import org.springframework.security.GrantedAuthorityImpl;import org.springframework.security.Authentication;import org.springframework.security.context.SecurityContextHolder;import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;import org.springframework.security.userdetails.User;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.junit.BeforeClass;
import org.flexpay.common.service.Roles;

import java.io.InputStream;

/**
 * Base class for all SpringFramework initialised beans aware tests
 */
@ContextConfiguration (locations = {"file:WEB-INF/applicationContext.xml"})
public abstract class SpringBeanAwareTestCase extends AbstractJUnit4SpringContextTests {

	protected Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier ("hibernateTemplate")
	protected HibernateTemplate hibernateTemplate;
	@Autowired
	@Qualifier("jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;

	protected InputStream getFileStream(@NonNls String relativePath) {
		return getClass().getClassLoader().getResourceAsStream(relativePath);
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
