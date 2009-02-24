package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TownName;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownService;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class TestTownHistoryBuilder extends SpringBeanAwareTestCase {

	@Autowired
	private TownHistoryBuilder historyBuilder;
	@Autowired
	@Qualifier ("townService")
	private TownService townService;
	@Autowired
	@Qualifier ("correctionsService")
	private CorrectionsService correctionsService;
	@Autowired
	private MasterIndexService masterIndexService;

	@Test
	public void testBuildDiff() {

		Diff diff = historyBuilder.diff(new Town(), new Town());
		assertTrue("Diff of two empty towns is not empty", diff.isEmpty());
	}

	@Test
	public void testBuildDiff3() {

		Diff diff = historyBuilder.diff(null, new Town());
		assertTrue("Diff of two empty towns is not empty", diff.isEmpty());
	}

	@Test
	public void testBuildDiff4() {

		Town town = townService.readFull(new Stub<Town>(3L));
		if (town == null) {
			throw new IllegalStateException("No town #3 found");
		}

		Diff diff = historyBuilder.diff(null, town);
		assertEquals("Invalid history builder", 2, diff.size());
	}

	@Test
	public void testPatch() {

		Town town = townService.readFull(new Stub<Town>(3L));
		if (town == null) {
			throw new IllegalStateException("No town #3 found");
		}

		Diff diff = historyBuilder.diff(null, town);
		Town newTown = new Town();
		historyBuilder.patch(newTown, diff);

		TownName name = newTown.getCurrentName();
		assertNotNull("Invalid patch, no name", name);
		assertEquals("Invalid name patch", 1, name.getTranslations().size());
		assertEquals("Invalid current name after patch", "Майкоп", name.getDefaultNameTranslation());

		TownType type = newTown.getCurrentType();
		assertNotNull("Invalid patch, no type", type);
		assertEquals("Invalid current type after patch", Long.valueOf(1L), type.getId());
	}

	@Before
	public void addTownTypeMasterCorrection() {
		correctionsService.save(getCityTypeMasterCorrection());
	}

	@After
	public void delTownTypeMasterCorrection() {
		correctionsService.delete(getCityTypeMasterCorrection());
	}

	private DataCorrection getCityTypeMasterCorrection() {
		TownType cityType = new TownType(1L);
		return correctionsService.getStub(
				masterIndexService.getNewMasterIndex(cityType),
				cityType,
				masterIndexService.getMasterSourceDescription());
	}
}
