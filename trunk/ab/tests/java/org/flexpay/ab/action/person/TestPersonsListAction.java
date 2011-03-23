package org.flexpay.ab.action.person;

import org.flexpay.ab.persistence.filters.PersonSearchFilter;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class TestPersonsListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private PersonsListAction action;

	@Test
	public void testAction() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid persons list size", action.getPersons().isEmpty());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullPersonSearchFilter() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid persons list size", action.getPersons().isEmpty());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testEmptyPersonSearchFilter() throws Exception {

		action.setPersonSearchFilter(new PersonSearchFilter(""));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid persons list size", action.getPersons().isEmpty());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testNotEmptyPersonSearchFilter1() throws Exception {

		action.setPersonSearchFilter(new PersonSearchFilter("g"));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid persons list size. Must be 0", action.getPersons().isEmpty());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testNotEmptyPersonSearchFilter2() throws Exception {

		action.setPersonSearchFilter(new PersonSearchFilter("ф"));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid persons list size", action.getPersons().isEmpty());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

}
