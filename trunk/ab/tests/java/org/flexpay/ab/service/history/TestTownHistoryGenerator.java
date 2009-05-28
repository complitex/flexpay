package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTownHistoryGenerator extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownHistoryGenerator generator;
	@Autowired
	private PersonsHistoryGenerator personsHistoryGenerator;

	@Test
	public void testGenerateTownHistory() {
		generator.generateFor(new Town(2L));
		personsHistoryGenerator.generate();
	}
}
