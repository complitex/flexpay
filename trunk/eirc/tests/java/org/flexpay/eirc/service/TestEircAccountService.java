package org.flexpay.eirc.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.filters.ApartmentFilter;
import org.flexpay.ab.persistence.filters.PersonSearchFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.EircAccount;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TestEircAccountService extends SpringBeanAwareTestCase {

	@Autowired
	protected EircAccountService eircAccountService;

	@Test
	public void testFindEircAccountByApartment() {

		ApartmentFilter apartmentFilter = new ApartmentFilter(330L);
		ArrayStack filters = CollectionUtils.arrayStack(apartmentFilter);

		List<EircAccount> accounts = eircAccountService.findAccounts(filters, new Page<EircAccount>());
		assertFalse("No eirc accounts found by apartment", accounts.isEmpty());
	}

	@Test
	public void testFindEircAccountByApartment2() {

		ApartmentFilter apartmentFilter = new ApartmentFilter(330L);
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
