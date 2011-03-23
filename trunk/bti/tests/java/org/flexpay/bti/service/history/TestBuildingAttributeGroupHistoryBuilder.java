package org.flexpay.bti.service.history;

import org.flexpay.bti.persistence.building.BuildingAttributeGroup;
import org.flexpay.bti.persistence.building.BuildingAttributeGroupName;
import org.flexpay.bti.service.BuildingAttributeGroupService;
import org.flexpay.bti.test.BtiSpringBeanAwareTestCase;
import org.flexpay.bti.test.TestData;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.util.config.ApplicationConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class TestBuildingAttributeGroupHistoryBuilder extends BtiSpringBeanAwareTestCase {

	@Autowired
	private BuildingAttributeGroupService groupService;
	@Autowired
	private BuildingAttributeGroupHistoryBuilder historyBuilder;

	@Test
	public void testPatchGroup() {

		BuildingAttributeGroup group = groupService.readFull(TestData.GROUP_1);
		assertNotNull("Group #1 not found", group);

		Diff diff = historyBuilder.diff(null, group);
		BuildingAttributeGroup copy = new BuildingAttributeGroup();
		historyBuilder.patch(copy, diff);

		for (Language language : ApplicationConfig.getLanguages()) {
			BuildingAttributeGroupName tr1 = group.getTranslation(language);
			BuildingAttributeGroupName tr2 = copy.getTranslation(language);
			assertSame("Invalid name patch for lang " + language, tr1 == null, tr2 == null);
			if (tr1 != null && tr2 != null) {
				assertSame("Invalid name patch for lang " + language, tr1.getName(), tr2.getName());
			}
		}
	}
}
