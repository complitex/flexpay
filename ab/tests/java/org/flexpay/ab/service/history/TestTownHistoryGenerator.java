package org.flexpay.ab.service.history;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.ab.persistence.Town;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTownHistoryGenerator extends SpringBeanAwareTestCase {

	@Autowired
	private TownHistoryGenerator generator;

	@Test
	public void testGenerateTownHistory() {
		generator.generateFor(new Town(2L));
	}
}
