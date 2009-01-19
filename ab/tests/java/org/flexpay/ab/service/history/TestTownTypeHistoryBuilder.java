package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTownTypeHistoryBuilder extends SpringBeanAwareTestCase {

	@Autowired
	private TownTypeHistoryBuilder historyBuilder;

	@Test
	public void testBuildDiff() {

		Diff diff = historyBuilder.diff(new TownType(), new TownType());
		assertTrue("Diff of two empty types is not empty", diff.isEmpty());
	}

	@Test
	public void testBuildDiff2() {

		Diff diff = historyBuilder.diff(null, new TownType());
		assertTrue("Diff of two empty types is not empty", diff.isEmpty());
	}

	@Test
	public void testBuildDiff3() {

		Diff diff = historyBuilder.diff(null, new TownType());
		assertTrue("Diff of two empty types is not empty", diff.isEmpty());
	}

	@Test
	public void testBuildDiff4() {

		TownType townType = new TownType(123L);
		townType.setId(123L);
		TownTypeTranslation translation = new TownTypeTranslation("Test translation");
		translation.setShortName("TT");

		townType.setTranslation(translation);
		assertFalse("No translations was added", townType.getTranslations().isEmpty());
		assertNotNull("No default translation", townType.getDefaultTranslation());

		Diff diff = historyBuilder.diff(null, townType);
		assertEquals("Invalid history builder", 2, diff.size());
	}

	@Test
	public void testPatch() {

		TownType townType = new TownType(123L);
		townType.setId(123L);
		TownTypeTranslation translation = new TownTypeTranslation("Test translation");
		translation.setShortName("TT");
		townType.setTranslation(translation);

		Diff diff = historyBuilder.diff(null, townType);

		TownType type = new TownType();
		historyBuilder.patch(type, diff);
		TownTypeTranslation translationNew = type.getDefaultTranslation();
		assertNotNull("No new default translation", translationNew);
		assertEquals("Invalid default name", "Test translation", translationNew.getName());
		assertEquals("Invalid default short name", "TT", translationNew.getShortName());
	}
}
