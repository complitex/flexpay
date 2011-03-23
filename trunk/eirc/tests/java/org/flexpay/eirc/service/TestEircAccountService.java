package org.flexpay.eirc.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.persistence.filters.ApartmentFilter;
import org.flexpay.ab.persistence.filters.PersonSearchFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertFalse;

public class TestEircAccountService extends EircSpringBeanAwareTestCase {

	@Autowired
	protected EircAccountService eircAccountService;

	@Test
	public void testFindEircAccountByApartment() {

		ApartmentFilter apartmentFilter = new ApartmentFilter(TestData.IVANOVA_27_330.getId());
		ArrayStack filters = CollectionUtils.arrayStack(apartmentFilter);

		List<EircAccount> accounts = eircAccountService.findAccounts(filters, new Page<EircAccount>());
		assertFalse("No eirc accounts found by apartment", accounts.isEmpty());
	}

	@Test
	public void testFindEircAccounts() {

		List<EircAccount> accounts = eircAccountService.findAccounts(
				CollectionUtils.arrayStack(new ApartmentFilter()), new Page<EircAccount>());
		assertFalse("No eirc accounts found", accounts.isEmpty());
	}

	@Test
	public void testFindEircAccountByApartment2() {

		ApartmentFilter apartmentFilter = new ApartmentFilter(TestData.IVANOVA_27_330.getId());
		ArrayStack filters = CollectionUtils.arrayStack(new PersonSearchFilter(), apartmentFilter);

		List<EircAccount> accounts = eircAccountService.findAccounts(filters, new Page<EircAccount>());
		assertFalse("No eirc accounts found by apartment", accounts.isEmpty());
	}

	@Test
	public void testFindEircAccountByConsumerInfoFIO() {

		PersonSearchFilter personSearchFilter = new PersonSearchFilter("иван");
		ArrayStack filters = CollectionUtils.arrayStack(personSearchFilter);

		List<EircAccount> accounts = eircAccountService.findAccounts(filters, new Page<EircAccount>());
		assertFalse("No eirc accounts found by fio", accounts.isEmpty());
	}

	@Test
	public void testFindEircAccountByPersonFIO() {

		PersonSearchFilter personSearchFilter = new PersonSearchFilter("михаил");
		ArrayStack filters = CollectionUtils.arrayStack(personSearchFilter);

		List<EircAccount> accounts = eircAccountService.findAccounts(filters, new Page<EircAccount>());
		assertFalse("No eirc accounts found by fio", accounts.isEmpty());
	}
}
