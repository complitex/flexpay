package org.flexpay.payments.action.monitor;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.util.TestCashboxUtil;
import org.flexpay.orgs.util.TestOrganizationUtil;
import org.flexpay.orgs.util.TestPaymentPointUtil;
import org.flexpay.payments.actions.monitor.PaymentPointDetailMonitorAction;
import org.flexpay.payments.actions.monitor.PaymentPointsListMonitorAction;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.flexpay.payments.util.TestOperationUtil;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.flexpay.payments.util.impl.PaymentsTestCashPaymentOperationUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.context.SecurityContextHolder;

import java.util.Set;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestPaymentPointDetailMonitorAction extends PaymentsSpringBeanAwareTestCase {
    @Autowired
    private PaymentPointDetailMonitorAction paymentPointDetailMonitorAction;

    @Autowired
    private PaymentPointsListMonitorAction paymentPointsListMonitorAction;

    @Autowired
    @Qualifier("paymentCollectorService")
    private PaymentCollectorService paymentCollectorService;

    @Autowired
    @Qualifier("paymentsTestPaymentPointUtil")
    private TestPaymentPointUtil paymentPointUtil;

    @Autowired
    @Qualifier("paymentsTestOrganizationUtil")
    private TestOrganizationUtil organizationUtil;

    @Autowired
    @Qualifier("paymentsTestCashPaymentOperationUtil")
    private TestOperationUtil operationUtil;

    @Autowired
    @Qualifier("paymentPointService")
    private PaymentPointService paymentPointService;

    private Long systemPaymentCollectorId;
    private Organization organization;
    private PaymentPoint paymentPoint;
    private Set<Operation> operations;

    @Before
    public void setUp() {
        operations = CollectionUtils.set();

        systemPaymentCollectorId = getPaymentCollectorId();

        organization = organizationUtil.create("123");
        assertNotNull("Did not create organization", organization);

        paymentPoint = paymentPointUtil.create(organization);
        assertNotNull("Did not create payment point", paymentPoint);
        setPaymentCollectorId(paymentPoint.getCollector().getId());
    }

    @After
    public void tearDown() {
        for (Operation operation : operations) {
            operationUtil.delete(operation);
        }
        setPaymentCollectorId(systemPaymentCollectorId);
        if (paymentPoint != null) {
            paymentPointUtil.delete(paymentPoint);
        }
        if (organization != null) {
            organizationUtil.delete(organization);
        }
    }

    @Test
    public void testPaymentPointIdDidNotSet() throws Exception {
        assertEquals("Action failed. Payment point did not set", FPActionSupport.ERROR, paymentPointDetailMonitorAction.execute());
    }

    @Test
    public void testNullablePaymentPointId() throws Exception {
        paymentPointDetailMonitorAction.setPaymentPointId(null);
        assertEquals("Action failed. Payment point id is null", FPActionSupport.ERROR, paymentPointDetailMonitorAction.execute());
    }

    @Test
    public void testNotExistPaymentPointId() throws Exception {
        paymentPointDetailMonitorAction.setPaymentPointId(String.valueOf(Long.MAX_VALUE));
        assertEquals("Action failed. Payment point do not exist", FPActionSupport.ERROR, paymentPointDetailMonitorAction.execute());
    }

    @Test
    public void testOk() throws Exception {
        paymentPointDetailMonitorAction.setPaymentPointId(String.valueOf(paymentPoint.getId()));
        assertEquals("Action failed", FPActionSupport.SUCCESS, paymentPointDetailMonitorAction.execute());
        assertNull("Payment point status is not empty", paymentPointDetailMonitorAction.getTradingDayControlPanel().getProcessStatus());
        assertTrue("Trading day do not open", paymentPointDetailMonitorAction.getTradingDayControlPanel().isTradingDayOpened());
        assertEquals("Cash box list is not empty", 0, paymentPointDetailMonitorAction.getCashboxes().size());
    }

    @Test
    public void testOk2() throws Exception {
        Cashbox cashbox1 = paymentPointUtil.addCashBox(paymentPoint, "test cash box");
        assertNotNull("Cash box did not create", cashbox1);

        addOperation(cashbox1, 100);
        addOperation(cashbox1, 224);

        Cashbox cashbox2 = paymentPointUtil.addCashBox(paymentPoint, "test cash box 2");
        assertNotNull("Cash box did not create", cashbox2);

        addOperation(cashbox2, 281);

        paymentPointDetailMonitorAction.setPaymentPointId(String.valueOf(paymentPoint.getId()));
        assertEquals("Action failed", FPActionSupport.SUCCESS, paymentPointDetailMonitorAction.execute());
        assertNull("Payment point status is not empty", paymentPointDetailMonitorAction.getTradingDayControlPanel().getProcessStatus());
        assertTrue("Trading day do not open", paymentPointDetailMonitorAction.getTradingDayControlPanel().isTradingDayOpened());
        assertEquals("Cash box list is empty", 2, paymentPointDetailMonitorAction.getCashboxes().size());

        assertEquals("Cash box failed", String.valueOf(cashbox1.getId()), paymentPointDetailMonitorAction.getCashboxes().get(0).getId());
        assertEquals("Cash box total sum failed", "324.00", paymentPointDetailMonitorAction.getCashboxes().get(0).getTotalSum());

        assertEquals("Cash box failed", String.valueOf(cashbox2.getId()), paymentPointDetailMonitorAction.getCashboxes().get(1).getId());
        assertEquals("Cash box total sum failed", "281.00", paymentPointDetailMonitorAction.getCashboxes().get(1).getTotalSum());

        assertEquals("Payment point total sum failed", "605.00", paymentPointDetailMonitorAction.getTotalSum());
    }

    private void addOperation(Cashbox cashbox, long summ) {
        Operation operation = operationUtil.create(cashbox, summ);
        assertNotNull("Operation did not create", operation);
        operations.add(operation);
    }

    @Test
    public void testStartedTradingDay() throws Exception {
        // Start trading day
        paymentPointsListMonitorAction.setPaymentPointId(String.valueOf(paymentPoint.getId()));
        paymentPointsListMonitorAction.setAction(PaymentPointsListMonitorAction.ENABLE);
        assertEquals("Action failed", FPActionSupport.SUCCESS, paymentPointsListMonitorAction.execute());
        paymentPoint = paymentPointService.read(Stub.stub(paymentPoint));
        assertNotNull("Payment point deleted", paymentPoint);
        assertNotNull("Trading day did not start", paymentPoint.getTradingDayProcessInstanceId());

        // Check status payment point in detail monitor
        paymentPointDetailMonitorAction.setPaymentPointId(String.valueOf(paymentPoint.getId()));
        assertEquals("Action failed", FPActionSupport.SUCCESS, paymentPointDetailMonitorAction.execute());
        assertNotNull("Payment point status is empty", paymentPointDetailMonitorAction.getTradingDayControlPanel().getProcessStatus());
    }

    private Long getPaymentCollectorId() {
        return ((PaymentsUserPreferences)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getPaymentCollectorId();
    }

    private void setPaymentCollectorId(Long paymentCollectorId) {
        ((PaymentsUserPreferences)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).setPaymentCollectorId(paymentCollectorId);
    }
}
