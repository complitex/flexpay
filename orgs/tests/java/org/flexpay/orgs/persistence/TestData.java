package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.Stub;

public abstract class TestData {

	public static final Stub<Organization> ORG_CN = new Stub<Organization>(4L);
	public static final Stub<Organization> ORG_TSZH = new Stub<Organization>(3L);

	public static final Stub<ServiceOrganization> SRV_ORG_UCHASTOK45 = new Stub<ServiceOrganization>(1L);

	public static final Stub<ServiceProvider> SRV_PROVIDER_CN = new Stub<ServiceProvider>(1L);

	public static final Stub<Bank> BANK_CN = new Stub<Bank>(1L);
	public static final Stub<Bank> BANK_EIRC = new Stub<Bank>(2L);

	public static final Stub<Subdivision> SUBDIVISION_1 = new Stub<Subdivision>(1L);

	public static final Stub<PaymentPoint> PAYMENT_POINT_1 = new Stub<PaymentPoint>(1L);
	public static final Stub<PaymentPoint> PAYMENT_POINT_2 = new Stub<PaymentPoint>(2L);
	public static final Stub<PaymentPoint> PAYMENT_POINT_3 = new Stub<PaymentPoint>(3L);

	public static final Stub<Cashbox> CASHBOX_1 = new Stub<Cashbox>(1L);
	public static final Stub<Cashbox> CASHBOX_2 = new Stub<Cashbox>(2L);
	public static final Stub<Cashbox> CASHBOX_3 = new Stub<Cashbox>(2L);
}
