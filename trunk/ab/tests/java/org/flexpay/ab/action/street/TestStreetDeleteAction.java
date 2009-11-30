package org.flexpay.ab.action.street;

import org.flexpay.ab.actions.street.StreetDeleteAction;
import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestNTDUtils.createSimpleStreet;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestStreetDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetDeleteAction action;
	@Autowired
	private StreetDao streetDao;

	@Test
	public void testNullObjectIds() throws Exception {

		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testDeleteStreets() throws Exception {

		Street street = createSimpleStreet("testName");

		streetDao.create(street);

		action.setObjectIds(set(street.getId()));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		street = streetDao.read(street.getId());
		assertFalse("Invalid status for street. Must be disabled", street.isActive());

		streetDao.delete(street);

	}

}
