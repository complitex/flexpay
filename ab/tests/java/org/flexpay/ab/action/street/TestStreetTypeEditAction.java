package org.flexpay.ab.action.street;

import org.flexpay.ab.actions.street.StreetTypeEditAction;
import org.flexpay.ab.dao.StreetTypeDao;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleStreetType;
import static org.flexpay.ab.util.TestUtils.initNames;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestStreetTypeEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetTypeEditAction action;
	@Autowired
	private StreetTypeDao streetTypeDao;

	@Test
	public void testNullStreetType() throws Exception {

		action.setStreetType(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullStreetTypeId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullNamesAndShortNames() throws Exception {

		action.setStreetType(new StreetType(0L));
		action.setNames(null);
		action.setShortNames(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());
		assertEquals("Invalid short names size for different languages", ApplicationConfig.getLanguages().size(), action.getShortNames().size());

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		action.setStreetType(new StreetType(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());
		assertEquals("Invalid short names size for different languages", ApplicationConfig.getLanguages().size(), action.getShortNames().size());

	}

	@Test
	public void testEditNotSubmit() throws Exception {

		action.setStreetType(new StreetType(TestData.STR_TYPE_STREET));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());
		assertEquals("Invalid short names size for different languages", ApplicationConfig.getLanguages().size(), action.getShortNames().size());

	}

	@Test
	public void testIncorrectData1() throws Exception {

		action.setStreetType(new StreetType(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setShortNames(initNames("345"));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testEditDefunctStreetType() throws Exception {

		action.setStreetType(new StreetType(121212L));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testEditDisabledStreetType() throws Exception {

		StreetType streetType = createSimpleStreetType("type2");
		streetType.disable();
		streetTypeDao.create(streetType);

		action.setStreetType(streetType);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		streetTypeDao.delete(streetType);

	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setStreetType(new StreetType(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("555"));
		action.setShortNames(initNames("666"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid street type id", action.getStreetType().getId() > 0);

		streetTypeDao.delete(action.getStreetType());
	}

	@Test
	public void testEditSubmit() throws Exception {

		StreetType street = createSimpleStreetType("type1");
		streetTypeDao.create(street);

		action.setStreetType(street);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("999"));
		action.setShortNames(initNames("000"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		String name = action.getStreetType().getDefaultTranslation().getName();
		assertEquals("Invalid street type name value", "999", name);

		streetTypeDao.delete(action.getStreetType());
	}

}