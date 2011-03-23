package org.flexpay.bti.service.history;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.bti.persistence.building.BuildingAttribute;
import org.flexpay.bti.persistence.building.BuildingAttributeConfig;
import org.flexpay.bti.persistence.building.BuildingAttributeType;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.bti.test.BtiSpringBeanAwareTestCase;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryBuilder;
import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.flexpay.common.util.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.ParseException;
import java.util.Set;
import java.util.SortedSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestBtiBuildingHistoryBuilder extends BtiSpringBeanAwareTestCase {

	@Autowired
	@Qualifier ("buildingHistoryBuilder")
	private HistoryBuilder<Building> historyBuilder;
	@Autowired
	@Qualifier ("buildingReferencesHistoryGenerator")
	private ReferencesHistoryGenerator<Building> referencesHistoryGenerator;
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private ObjectsFactory objectsFactory;
	@Autowired
	private BuildingAttributeTypeService attributeTypeService;

	@Test
	public void testPatchAttributes() throws ParseException {
		BtiBuilding building = (BtiBuilding) buildingService.readFull(TestData.IVANOVA_27);
		assertNotNull("IVANOVA_27 not found", building);

		referencesHistoryGenerator.generateReferencesHistory(building);

		Diff diff = historyBuilder.diff(null, building);
		BtiBuilding copy = (BtiBuilding) objectsFactory.newBuilding();

		historyBuilder.patch(copy, diff);
		validateAttrtibutes(building, copy);

		BuildingAttributeType typeBuildYear = attributeTypeService.findTypeByName(
				BuildingAttributeConfig.ATTR_BUILD_YEAR);
		assertNotNull("No build year attribute found");
		BuildingAttribute attribute = new BuildingAttribute();
		attribute.setAttributeType(typeBuildYear);
		attribute.setIntValue(1953);
		building.setTmpAttributeForDate(attribute, DateUtil.parseDate("1999/01/01"));

		diff = historyBuilder.diff(copy, building);
		historyBuilder.patch(copy, diff);
		validateAttrtibutes(building, copy);
	}

	private void validateAttrtibutes(BtiBuilding building, BtiBuilding copy) {
		Set<BuildingAttributeType> types = building.attributeTypes();
		assertEquals("Wrong attribute types number", types, copy.attributeTypes());

		for (BuildingAttributeType type : types) {
			SortedSet<BuildingAttribute> attributes = building.attributesOfType(type);
			for (BuildingAttribute attribute : attributes) {
				BuildingAttribute beginAttribute = copy.getAttributeForDate(type, attribute.getBegin());
				assertNotNull("No value for begin, type: " + type, beginAttribute);
				assertEquals("invalid patch 1, type: " + type, attribute.value(), beginAttribute.value());

				BuildingAttribute endAttribute = copy.getAttributeForDate(type, attribute.getEnd());
				assertNotNull("No value for end, type: " + type, endAttribute);
				assertEquals("invalid patch 2, type: " + type, attribute.value(), endAttribute.value());
			}
		}
	}
}
