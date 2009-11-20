package org.flexpay.ab.action.town;

import org.flexpay.ab.actions.town.TownDeleteAction;
import org.flexpay.ab.dao.TownDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.util.CollectionUtils.set;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTownDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownDeleteAction action;
	@Autowired
	private TownDao townDao;

	@Test
	public void testDeleteTowns() throws Exception {

		Town town = new Town();
		TownName townName = new TownName();
		for (Language lang : ApplicationConfig.getLanguages()) {
			townName.setTranslation(new TownNameTranslation("testName", lang));
		}
		town.setNameForDate(townName, DateUtil.now());
		town.setParent(new Region(TestData.REGION_NSK.getId()));

		townDao.create(town);

		action.setObjectIds(set(town.getId()));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		town = townDao.read(town.getId());

		assertFalse("Invalid status for town. Must be disabled", town.isActive());

		townDao.delete(town);

	}

}
