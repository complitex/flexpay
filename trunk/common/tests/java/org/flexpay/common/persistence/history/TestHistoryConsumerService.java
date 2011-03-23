package org.flexpay.common.persistence.history;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.HistoryConsumerService;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertFalse;

public class TestHistoryConsumerService extends SpringBeanAwareTestCase {

	@Autowired
	private HistoryConsumerService service;

	@Test
	public void testGetNewRecords() {
		FetchRange range = new FetchRange();
		Stub<HistoryConsumer> stub = new Stub<HistoryConsumer>(1L);
		List<Diff> diffs = service.findNewDiffs(stub, range);

		assertFalse("No diffs found", diffs.isEmpty());
	}
}
