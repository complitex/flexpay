package org.flexpay.common.dao;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.dao.paging.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class TestMeasureUnitDao extends SpringBeanAwareTestCase {

	@Autowired
	@Qualifier ("measureUnitDao")
	private MeasureUnitDao measureUnitDao;

	@Test
	public void testCallCollection() {

		Long[] ids1 = {1L, 2L};
		List<Long> ids2 = CollectionUtils.list(1L, 2L);
		measureUnitDao.listUnitsTest(1L, new Page(), ids2, 2L, ids1);
	}

	@Test
	public void testCallArray() {

		Long[] ids1 = {1L, 2L};
		List<Long> ids2 = CollectionUtils.list(1L, 2L);
		measureUnitDao.listUnitsTest(new Page(), 1L, ids1, 2L, ids2);
	}
}
