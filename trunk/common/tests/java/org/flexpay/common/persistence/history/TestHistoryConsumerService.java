package org.flexpay.common.persistence.history;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.service.HistoryConsumerService;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.Stub;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.util.List;

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
