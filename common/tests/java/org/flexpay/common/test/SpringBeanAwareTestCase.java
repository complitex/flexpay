package org.flexpay.common.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.jetbrains.annotations.NonNls;

import javax.sql.DataSource;
import java.io.InputStream;

/**
 * Base class for all SpringFramework initialised beans aware tests
 */
@ContextConfiguration(locations = {"file:WEB-INF/applicationContext.xml"})
@TransactionConfiguration(transactionManager="transactionManager")
public abstract class SpringBeanAwareTestCase extends AbstractTransactionalJUnit4SpringContextTests {

	@NonNls
	protected HibernateTemplate hibernateTemplate;

	@Override
	@Autowired
	public void setDataSource(@Qualifier("dataSource")DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Autowired
	public void setHibernateTemplate(@Qualifier("hibernateTemplate")HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	protected InputStream getFileStream(String relativePath) {
		return getClass().getClassLoader().getResourceAsStream(relativePath);
	}
}
