package org.flexpay.eirc.dao;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collection;

public class TestServedBuildingDao extends EircSpringBeanAwareTestCase {

	@Autowired
	@Qualifier ("servedBuildingDao")
	private ServedBuildingDao servedBuildingDao;

	@Test
	public void testCallCollection() {
		Collection<Long> ids = CollectionUtils.list(1L, 2L);
		servedBuildingDao.findServedBuildings(ids);
	}

}
