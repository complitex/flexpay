package org.flexpay.eirc.service.exchange;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.exchange.Operation;

public interface ExchangeHelper {

	/**
	 * Get address group delimited with {@link Operation#ADDRESS_DELIMITER}
	 *
	 * @param apartment Apartment stub
	 * @return Address group
	 * @throws FlexPayException if failure occurs
	 */
	String getAddressGroup(Apartment apartment) throws FlexPayException;

	/**
	 * Get first, middle, last person names group delimited with {@link Operation#FIO_DELIMITER}
	 *
	 * @param personStub Person stub
	 * @return FIO group
	 */
	String getFIOGroup(Stub<Person> personStub);
}
