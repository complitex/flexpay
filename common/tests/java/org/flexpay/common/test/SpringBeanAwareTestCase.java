package org.flexpay.common.test;

import org.jetbrains.annotations.NonNls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
