package org.flexpay.payments.action.monitor;

import static junit.framework.Assert.assertNotNull;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.payments.actions.monitor.PaymentPointsListMonitorAction;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.flexpay.payments.util.impl.PaymentsTestOrganizationUtil;
import org.flexpay.payments.util.impl.PaymentsTestPaymentCollectorUtil;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.context.SecurityContextHolder;

import javax.annotation.Resource;


public class TestPaymentPointsListMonitorAction extends PaymentsSpringBeanAwareTestCase {
    @Autowired
    private PaymentPointsListMonitorAction paymentPointsListMonitorAction;

    @Autowired
    @Qualifier("paymentCollectorService")
    private PaymentCollectorService paymentCollectorService;

    @Autowired
    @Resource(name="paymentsTestPaymentCollectorUtil")
    private PaymentsTestPaymentCollectorUtil paymentCollectorUtil;

    @Autowired
    @Resource(name="paymentsTestOrganizationUtil")
    private PaymentsTestOrganizationUtil organizationUtil;

    private Long systemPaymentCollectorId;
    private Organization organization;
    private PaymentCollector paymentCollector;

    @Before
    public void setUp() throws FlexPayExceptionContainer {
        systemPaymentCollectorId = getPaymentCollectorId();

        organization = organizationUtil.create("123");
        assertNotNull("Did not create organization", organization);

        paymentCollector = paymentCollectorUtil.create(organization);
        assertNotNull("Did not create payment collector", paymentCollector);
        setPaymentCollectorId(paymentCollector.getId());
    }

    @After
    public void tearDown() {
        setPaymentCollectorId(systemPaymentCollectorId);
        if (paymentCollector != null) {
            paymentCollectorUtil.delete(paymentCollector);
        }
        if (organization != null) {
            organizationUtil.delete(organization);
        }
    }

    @Test
    public void testNullablePaymentCollectorId() throws Exception {
        setPaymentCollectorId(null);
        assertEquals("Action failed", FPActionSupport.ERROR, paymentPointsListMonitorAction.execute());
    }

    @Test
    public void testNotExistCollectorId() throws Exception {
        setPaymentCollectorId(Long.MAX_VALUE);
        assertEquals("Action failed", FPActionSupport.ERROR, paymentPointsListMonitorAction.execute());
    }

    @Test
    public void testOk() throws Exception {
        assertEquals("Action failed", FPActionSupport.SUCCESS, paymentPointsListMonitorAction.execute());
    }



    private Long getPaymentCollectorId() {
        return ((PaymentsUserPreferences)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getPaymentCollectorId();
    }

    private void setPaymentCollectorId(Long paymentCollectorId) {
        ((PaymentsUserPreferences)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).setPaymentCollectorId(paymentCollectorId);
    }
}
