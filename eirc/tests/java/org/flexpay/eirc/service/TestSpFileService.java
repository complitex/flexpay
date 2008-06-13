package org.flexpay.eirc.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.test.SpringBeanAwareTestCase;

public class TestSpFileService extends SpringBeanAwareTestCase {

	@Autowired
	private SpFileService fileService;

	@Test
	public void testFindRecordsForProcessing() {

		Page<SpRegistryRecord> pager = new Page<SpRegistryRecord>(500, 4);
		fileService.getRecordsForProcessing(new SpRegistry(33L), pager);

		pager.setPageNumber(pager.getPageNumber() + 1);
		fileService.getRecordsForProcessing(new SpRegistry(33L), pager);
	}
}
