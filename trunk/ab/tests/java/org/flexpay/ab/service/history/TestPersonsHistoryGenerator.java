package org.flexpay.ab.service.history;

import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestPersonsHistoryGenerator extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonsHistoryGenerator historyGenerator;

	@Test
	public void testGeneratePersonsHistory() {

		historyGenerator.generate();
	}
}
