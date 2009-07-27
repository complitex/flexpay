package org.flexpay.orgs.test;

import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.common.persistence.Stub;

public abstract class TestData {

	public static final Stub<Organization> ORG_CN = new Stub<Organization>(4L);

	public static final Stub<PaymentPoint> PAYMENT_POINT_1 = new Stub<PaymentPoint>(1L);
	public static final Stub<PaymentPoint> PAYMENT_POINT_2 = new Stub<PaymentPoint>(2L);
}
