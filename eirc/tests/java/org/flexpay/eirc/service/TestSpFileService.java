package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.service.RegistryFileService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestSpFileService extends SpringBeanAwareTestCase {

	@Autowired
	protected RegistryFileService fileService;

	@Test
	public void testFindRecordsForProcessing() {

		FetchRange range = new FetchRange(500);
		fileService.getRecordsForProcessing(new Stub<Registry>(33L), range);

		range.nextPage();
		fileService.getRecordsForProcessing(new Stub<Registry>(33L), range);
	}

}
