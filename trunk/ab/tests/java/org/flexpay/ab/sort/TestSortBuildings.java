package org.flexpay.ab.sort;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.junit.Assert.assertNotSame;

public class TestSortBuildings extends AbSpringBeanAwareTestCase {

	@Autowired
	private AddressAttributeTypeService attributeTypeService;
	@Autowired
	private AddressService addressService;

	@Test
	public void testSortBuildingsByNumberAndBulk() {
		String hql = "select distinct b from Building b " +
					 "	left join b.buildingses bs " +
					 "	left join bs.buildingAttributes ba1 with (ba1.buildingAttributeType.id=?) " +
					 "	left join bs.buildingAttributes ba2 with (ba2.buildingAttributeType.id=?) " +
					 "where b.status=0 and bs.status=0 and bs.primaryStatus=true and bs.street.id=? " +
					 "order by lpad(convert(ifnull(ba1.value, '0'), UNSIGNED), 10, '0'), lpad(convert(ifnull(ba2.value, '0'), UNSIGNED), 10, '0') ";

		StopWatch watch = new StopWatch();

		Long streetId = TestData.IVANOVA.getId();
		Long typeNumberId = ApplicationConfig.getBuildingAttributeTypeNumber().getId();
		Long typebulkId = ApplicationConfig.getBuildingAttributeTypeBulk().getId();
		Long[] params = {typeNumberId, typebulkId, streetId};

		watch.start();
		List<?> result = jpaTemplate.find(hql, params);
		int size = result.size();
		watch.stop();

		log.debug("Time spent sorting buildings by number and bulk {}", watch);
		assertNotSame("BuildingAddress not found.", 0, size);
	}

	@Test
	public void testSortBuildings() throws Exception {

		List<AddressAttributeType> types = attributeTypeService.getAttributeTypes();

		StringBuilder hql = new StringBuilder("select distinct b from Building b " +
					 "	left join b.buildingses bs ");
		for (AddressAttributeType type : types) {
			Long id = type.getId();
			hql.append("  left join bs.buildingAttributes ba").append(id).
					append(" with (ba").append(id).append(".buildingAttributeType.id=").append(id).append(") ");
		}
		hql.append("where b.status=0 and bs.status=0 and bs.street.id=? " +
				   "order by ");

		boolean firstOrderBy = true;
		for (AddressAttributeType type : types) {
			Long id = type.getId();
			if (!firstOrderBy) {
				hql.append(", ");
			} else {
				firstOrderBy = false;
			}
			hql.append("lpad(convert(ifnull(ba").append(id).append(".value, '0'), UNSIGNED), 10, '0') ");
		}

		StopWatch watch = new StopWatch();

		Long streetId = TestData.IVANOVA.getId();
		Long[] params = {streetId};

		watch.start();
		@SuppressWarnings ({"unchecked"})
		List<Building> result = (List<Building>) jpaTemplate.find(hql.toString(), params);
		int size = result.size();
		watch.stop();

		log.debug("Time spent sorting buildings {}, hql:\n{}", watch, hql);
		assertNotSame("BuildingAddress not found.", 0, size);

		for (Building building : result) {
			log.debug(addressService.getBuildingAddressOnStreet(stub(building), TestData.IVANOVA, null));
		}
	}
}
