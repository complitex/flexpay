package org.flexpay.ab.action.region;

import org.flexpay.ab.actions.region.RegionDeleteAction;
import org.flexpay.ab.dao.RegionDao;
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

public class TestRegionDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private RegionDeleteAction action;
	@Autowired
	private RegionDao regionDao;

	@Test
	public void testDeleteRegions() throws Exception {

		Region region = new Region();
		RegionName regionName = new RegionName();
		for (Language lang : ApplicationConfig.getLanguages()) {
			regionName.setTranslation(new RegionNameTranslation("testName", lang));
		}
		region.setNameForDate(regionName, DateUtil.now());
		region.setParent(new Country(TestData.COUNTRY_RUS.getId()));

		regionDao.create(region);

		action.setObjectIds(set(region.getId()));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		region = regionDao.read(region.getId());

		assertFalse("Invalid status for region. Must be disabled", region.isActive());

		regionDao.delete(region);
	}

}
