package org.flexpay.common.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.sql.DataSource;

/**
 * Base class for all SpringFramework initialised beans aware tests
 */
@ContextConfiguration(locations = {"file:WEB-INF/applicationContext.xml"})
@TransactionConfiguration(transactionManager="transactionManager")
public abstract class SpringBeanAwareTestCase extends AbstractTransactionalJUnit4SpringContextTests {

	@Override
	@Autowired
	public void setDataSource(@Qualifier("dataSource")DataSource dataSource) {
		super.setDataSource(dataSource);
	}
}
