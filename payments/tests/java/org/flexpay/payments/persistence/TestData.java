package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.Stub;

public class TestData {

	public static final Stub<Operation> OPERATION = new Stub<Operation>(1L);
	public static final Stub<Operation> OPERATION_2 = new Stub<Operation>(2L);

	public static final Stub<Service> SERVICE_KVARPLATA = new Stub<Service>(1L);
	public static final Stub<Service> SERVICE_TERRITORY_CLEANUP = new Stub<Service>(2L);
}
