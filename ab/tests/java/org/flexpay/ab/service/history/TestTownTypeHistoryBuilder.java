package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.junit.Assert.assertTrue;
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
}
