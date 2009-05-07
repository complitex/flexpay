package org.flexpay.ab.service.history;

import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;

public class TestPersonsHistoryGenerator extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonsHistoryGenerator historyGenerator;

	@Test
	public void testGeneratePersonsHistory() {

		historyGenerator.generate();
	}
}
