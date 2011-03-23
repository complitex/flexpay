package org.flexpay.common.test;

import org.flexpay.common.util.SecurityUtil;
import org.junit.Before;
import org.junit.BeforeClass;
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
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.List;

import static org.flexpay.common.service.Roles.*;

/**
 * Base class for all SpringFramework initialised beans aware tests
 */
@ContextConfiguration (locations = {
		"file:WEB-INF/applicationContext.xml"
		, "file:WEB-INF/applicationContext-security-opensso.xml"
		, "file:WEB-INF/common/configs/spring/history/beans-ws-server-test.xml"
})
@TransactionConfiguration (transactionManager = "transactionManager")
public abstract class TransactionalSpringBeanAwareTestCase extends AbstractTransactionalJUnit4SpringContextTests {

	protected Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier ("hibernateTemplate")
	protected HibernateTemplate hibernateTemplate;
	@Qualifier ("jdbcTemplate")
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	private Scheduler scheduler;

	@Override
	@Autowired
	public void setDataSource(@Qualifier ("dataSource") DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	protected InputStream getFileStream(String relativePath) {
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
		List<GrantedAuthority> authorities = SecurityUtil.auths(
				BASIC,
				PROCESS_DEFINITION_UPLOAD_NEW,
				PROCESS_READ
		);
		User user = new User("test", "test", true, true, true, true, authorities);
		Authentication auth = new AnonymousAuthenticationToken("key", user, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
