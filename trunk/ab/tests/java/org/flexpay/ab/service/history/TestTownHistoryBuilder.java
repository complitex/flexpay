package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TownName;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.*;

public class TestTownHistoryBuilder extends AbSpringBeanAwareTestCase {

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
		assertEquals("Invalid history builder", 4, diff.size());
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
		assertEquals("Invalid current type after patch", Long.valueOf(4L), type.getId());
	}

	@Before
	public void addTownTypeMasterCorrection() {
		correctionsService.save(getTypeMasterCorrection(1L));
		correctionsService.save(getTypeMasterCorrection(2L));
		correctionsService.save(getTypeMasterCorrection(3L));
		correctionsService.save(getTypeMasterCorrection(4L));
	}

	@After
	public void delTownTypeMasterCorrection() {
		correctionsService.delete(getTypeMasterCorrection(1L));
		correctionsService.delete(getTypeMasterCorrection(2L));
		correctionsService.delete(getTypeMasterCorrection(3L));
		correctionsService.delete(getTypeMasterCorrection(4L));
	}

	private DataCorrection getTypeMasterCorrection(Long typeId) {
		TownType type = new TownType(typeId);
		return correctionsService.getStub(
				masterIndexService.getNewMasterIndex(type),
				type,
				masterIndexService.getMasterSourceDescriptionStub());
	}
}
