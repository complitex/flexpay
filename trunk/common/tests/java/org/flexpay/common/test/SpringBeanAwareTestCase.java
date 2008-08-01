package org.flexpay.common.test;

import org.jetbrains.annotations.NonNls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.io.InputStream;

/**
 * Base class for all SpringFramework initialised beans aware tests
 */
@ContextConfiguration (locations = {"file:WEB-INF/applicationContext.xml"})
public abstract class SpringBeanAwareTestCase extends AbstractJUnit4SpringContextTests {

	@NonNls
	protected HibernateTemplate hibernateTemplate;
	protected JdbcTemplate jdbcTemplate;

	@Autowired
	public void setJdbcTemplate(@Qualifier ("jdbcTemplate")JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Autowired
	public void setHibernateTemplate(@Qualifier ("hibernateTemplate")HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	protected InputStream getFileStream(@NonNls String relativePath) {
		return getClass().getClassLoader().getResourceAsStream(relativePath);
	}
}
