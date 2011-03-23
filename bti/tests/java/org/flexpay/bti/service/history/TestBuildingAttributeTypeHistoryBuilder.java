package org.flexpay.bti.service.history;

import org.flexpay.bti.persistence.building.BuildingAttributeType;
import org.flexpay.bti.persistence.building.BuildingAttributeTypeEnum;
import org.flexpay.bti.persistence.building.BuildingAttributeTypeName;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.bti.test.BtiSpringBeanAwareTestCase;
import org.flexpay.bti.test.TestData;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.DiffProcessor;
import org.flexpay.common.util.config.ApplicationConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.*;

public class TestBuildingAttributeTypeHistoryBuilder extends BtiSpringBeanAwareTestCase {

	@Autowired
	private BuildingAttributeTypeService attributeTypeService;
	@Autowired
	private BuildingAttributeTypeHistoryBuilder historyBuilder;
	@Autowired
	private BuildingAttributeTypeReferencesHistoryGenerator referencesHistoryGenerator;
	@Autowired
	@Qualifier ("buildingAttributeTypeDiffProcessor")
	private DiffProcessor<BuildingAttributeType> diffProcessor;

	@Test
	public void testPatchSimpleType() throws Exception {

		BuildingAttributeType type = attributeTypeService.readFull(TestData.ATTR_SECTION_NUMBER);
		assertNotNull("Type ATTR_SECTION_NUMBER not found", type);

		generateReferenecensesHistory(type);
		Diff diff = historyBuilder.diff(null, type);
		diffProcessor.onCreate(type, diff);

		BuildingAttributeType copy = (BuildingAttributeType) Class.forName(diff.getObjectTypeName()).newInstance();
		historyBuilder.patch(copy, diff);

		for (Language language : ApplicationConfig.getLanguages()) {
			BuildingAttributeTypeName tr1 = type.getTranslation(language);
			BuildingAttributeTypeName tr2 = copy.getTranslation(language);
			assertSame("Invalid name patch for lang " + language, tr1 == null, tr2 == null);
			if (tr1 != null && tr2 != null) {
				assertSame("Invalid name patch for lang " + language, tr1.getName(), tr2.getName());
			}
		}

		assertEquals("Invalid unique code patch", type.getUniqueCode(), copy.getUniqueCode());
		assertEquals("Invalid tmp flag patch", type.isTemp(), copy.isTemp());
		assertEquals("Invalid group patch", type.getGroup(), copy.getGroup());
	}

	@Test
	public void testPatchEnumType() throws Exception {

		BuildingAttributeTypeEnum type = (BuildingAttributeTypeEnum) attributeTypeService
				.readFull(TestData.ATTR_HOUSE_TYPE_ENUM);
		assertNotNull("Type ATTR_HOUSE_TYPE_ENUM not found", type);

		generateReferenecensesHistory(type);
		Diff diff = historyBuilder.diff(null, type);
		diffProcessor.onCreate(type, diff);

		BuildingAttributeTypeEnum copy = (BuildingAttributeTypeEnum) Class.forName(diff.getObjectTypeName()).newInstance();
		historyBuilder.patch(copy, diff);

		for (Language language : ApplicationConfig.getLanguages()) {
			BuildingAttributeTypeName tr1 = type.getTranslation(language);
			BuildingAttributeTypeName tr2 = copy.getTranslation(language);
			assertSame("Invalid name patch for lang " + language, tr1 == null, tr2 == null);
			if (tr1 != null && tr2 != null) {
				assertSame("Invalid name patch for lang " + language, tr1.getName(), tr2.getName());
			}
		}

		assertEquals("Invalid unique code patch", type.getUniqueCode(), copy.getUniqueCode());
		assertEquals("Invalid tmp flag patch", type.isTemp(), copy.isTemp());
		assertEquals("Invalid group patch", type.getGroup(), copy.getGroup());

		assertEquals("Invalid enum values patch", type.getSortedValues(), copy.getSortedValues());
	}

	private void generateReferenecensesHistory(BuildingAttributeType type) {
		referencesHistoryGenerator.generateReferencesHistory(type);
	}
}
