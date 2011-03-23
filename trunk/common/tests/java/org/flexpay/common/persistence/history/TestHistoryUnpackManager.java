package org.flexpay.common.persistence.history;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.FPFileUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestHistoryUnpackManager extends SpringBeanAwareTestCase {

	@Autowired
	private HistoryUnpackManager unpackManager;
	@Autowired
	private FPFileService fileService;

	@Test
	public void testGetLastPacks() throws Exception {

		assertTrue("Packs found", unpackManager.getNextPacks().isEmpty());

		ExternalHistoryPack pack = getNewPack(1L);
		unpackManager.create(pack);

		List<ExternalHistoryPack> newPacks = unpackManager.getNextPacks();
		assertEquals("just created pack not found", 1, newPacks.size());
		unpackManager.setLastUnpacked(newPacks.get(0));

		assertTrue("Packs found 2", unpackManager.getNextPacks().isEmpty());

		unpackManager.create(getNewPack(2L));
		unpackManager.create(getNewPack(3L));
		unpackManager.create(getNewPack(4L));
		unpackManager.create(getNewPack(5L));
		newPacks = unpackManager.getNextPacks();
		assertEquals("just created packs not found", 4, newPacks.size());

		unpackManager.setLastUnpacked(newPacks.get(2));
		newPacks = unpackManager.getNextPacks();
		assertEquals("Setting last unpacked failed", 1, newPacks.size());
	}

	private ExternalHistoryPack getNewPack(Long groupId) throws Exception {

		ExternalHistoryPack pack = new ExternalHistoryPack();

		FPFile file = new FPFile();
		file.setModule(fileService.getModuleByName("common"));
		file.setOriginalName("test.xml.gz");
		FPFileUtil.createEmptyFile(file);
		fileService.create(file);

		pack.setFile(file);
		pack.setConsumptionGroupId(groupId);
		pack.setSourceInstanceId("TEST_SOURCE");

		return pack;
	}
}
