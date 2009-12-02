package org.flexpay.ab.action.town;

import org.flexpay.ab.actions.town.TownTypeEditAction;
import org.flexpay.ab.dao.TownTypeDao;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleTownType;
import static org.flexpay.ab.util.TestUtils.initNames;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTownTypeEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownTypeEditAction action;
	@Autowired
	private TownTypeDao townTypeDao;

	@Test
	public void testNullTownType() throws Exception {

		action.setTownType(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullTownTypeId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullNamesAndShortNames() throws Exception {

		action.setTownType(new TownType(0L));
		action.setNames(null);
		action.setShortNames(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());
		assertEquals("Invalid short names size for different languages", ApplicationConfig.getLanguages().size(), action.getShortNames().size());

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		action.setTownType(new TownType(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());
		assertEquals("Invalid short names size for different languages", ApplicationConfig.getLanguages().size(), action.getShortNames().size());

	}

	@Test
	public void testEditNotSubmit() throws Exception {

		action.setTownType(new TownType(TestData.TOWN_TYPE_CITY));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());
		assertEquals("Invalid short names size for different languages", ApplicationConfig.getLanguages().size(), action.getShortNames().size());

	}

	@Test
	public void testIncorrectData1() throws Exception {

		action.setTownType(new TownType(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testIncorrectData2() throws Exception {

		action.setTownType(new TownType(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setShortNames(initNames("345"));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testEditDefunctTownType() throws Exception {

		action.setTownType(new TownType(121212L));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testEditDisabledTownType() throws Exception {

		TownType townType = createSimpleTownType("type2");
		townType.disable();
		townTypeDao.create(townType);

		action.setTownType(townType);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		townTypeDao.delete(townType);

	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setTownType(new TownType(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("555"));
		action.setShortNames(initNames("666"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid town type id", action.getTownType().getId() > 0);

		townTypeDao.delete(action.getTownType());
	}

	@Test
	public void testEditSubmit() throws Exception {

		TownType town = createSimpleTownType("type1");
		townTypeDao.create(town);

		action.setTownType(town);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("999"));
		action.setShortNames(initNames("000"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		String name = action.getTownType().getDefaultTranslation().getName();
		assertEquals("Invalid town type name value", "999", name);
		String shortName = action.getTownType().getDefaultTranslation().getShortName();
		assertEquals("Invalid town type short name value", "000", shortName);

		townTypeDao.delete(action.getTownType());
	}

}
