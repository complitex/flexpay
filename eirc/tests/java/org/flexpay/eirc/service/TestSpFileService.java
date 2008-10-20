package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.RegistryRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestSpFileService extends SpringBeanAwareTestCase {

	@Autowired
	protected RegistryFileService fileService;

	@Test
	public void testFindRecordsForProcessing() {

		Page<RegistryRecord> pager = new Page<RegistryRecord>(500, 4);
		fileService.getRecordsForProcessing(new Stub<SpRegistry>(33L), pager, CollectionUtils.ar(null, 21000L));

		pager.setPageNumber(pager.getPageNumber() + 1);
		fileService.getRecordsForProcessing(new Stub<SpRegistry>(33L), pager, CollectionUtils.ar(0L, 21000L));
	}
}
