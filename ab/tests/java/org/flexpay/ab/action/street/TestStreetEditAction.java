package org.flexpay.ab.action.street;

import org.flexpay.ab.actions.street.StreetEditAction;
import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestNTDUtils.createSimpleStreet;
import static org.flexpay.ab.util.TestNTDUtils.initNames;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestStreetEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetEditAction action;
	@Autowired
	private StreetDao streetDao;

	@Test
	public void testNullStreet() throws Exception {

		action.setStreet(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullStreetId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullNames() throws Exception {

		action.setStreet(new Street(0L));
		action.setNames(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", DateUtil.now(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testNullBeginDateFilter() throws Exception {

		action.setStreet(new Street(0L));
		action.setBeginDateFilter(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", DateUtil.now(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		action.setStreet(new Street(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", DateUtil.now(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testEditNotSubmit() throws Exception {

		action.setStreet(new Street(TestData.IVANOVA.getId()));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid beginDateFilter value", action.getStreet().getCurrentNameTemporal().getBegin(), action.getBeginDateFilter().getDate());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testIncorrectData1() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testIncorrectData2() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setTownFilter(TestData.TOWN_NSK.getId());
		action.setBeginDateFilter(null);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testIncorrectData3() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setTownFilter(-10L);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testEditDefunctStreet() throws Exception {

		action.setStreet(new Street(121212L));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testEditDisabledStreet() throws Exception {

		Street street = createSimpleStreet("testName");
		street.setStatus(DomainObjectWithStatus.STATUS_DISABLED);

		streetDao.create(street);

		action.setStreet(street);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		streetDao.delete(street);

	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setStreet(new Street(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setTownFilter(TestData.TOWN_NSK.getId());
		action.setNames(initNames("123"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid street id", action.getStreet().getId() > 0);

		streetDao.delete(action.getStreet());
	}

	@Test
	public void testEditSubmit() throws Exception {

		Street street = createSimpleStreet("testName");

		streetDao.create(street);

		action.setStreet(street);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setTownFilter(TestData.TOWN_NSK.getId());
		action.setBeginDateFilter(new BeginDateFilter(DateUtil.next(DateUtil.now())));
		action.setNames(initNames("123"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		String name = action.getStreet().getNameForDate(DateUtil.next(DateUtil.now())).getDefaultNameTranslation();
		assertEquals("Invalid street name value", "123", name);

		streetDao.delete(action.getStreet());
	}

}
