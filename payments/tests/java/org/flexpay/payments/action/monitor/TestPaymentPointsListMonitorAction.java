package org.flexpay.payments.action.monitor;

import static junit.framework.Assert.assertNotNull;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.util.TestOrganizationUtil;
import org.flexpay.orgs.util.TestPaymentPointUtil;
import org.flexpay.payments.actions.monitor.PaymentPointsListMonitorAction;
import org.flexpay.payments.actions.monitor.data.PaymentPointMonitorContainer;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.flexpay.payments.util.TestOperationUtil;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.flexpay.payments.util.impl.PaymentsTestCashPaymentOperationUtil;
import org.flexpay.payments.util.impl.PaymentsTestOrganizationUtil;
import org.flexpay.payments.util.impl.PaymentsTestPaymentPointUtil;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.context.SecurityContextHolder;
import org.flexpay.common.process.Process;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;


public class TestPaymentPointsListMonitorAction extends PaymentsSpringBeanAwareTestCase {
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

    @Autowired
    private ProcessManager processManager;

    private Long systemPaymentCollectorId;
    private Organization organization;
    private PaymentPoint paymentPoint;
    private Organization otherOrganization;
    private PaymentPoint otherPaymentPoint;

    private Set<Operation> operations;

    @Before
    public void setUp() throws FlexPayExceptionContainer {
        systemPaymentCollectorId = getPaymentCollectorId();

        organization = organizationUtil.create("123");
        assertNotNull("Did not create organization", organization);

        paymentPoint = paymentPointUtil.create(organization);
        assertNotNull("Did not create payment point", paymentPoint);
        setPaymentCollectorId(paymentPoint.getCollector().getId());

        otherOrganization = organizationUtil.create("124");
        assertNotNull("Did not create organization", otherOrganization);

        otherPaymentPoint = paymentPointUtil.create(otherOrganization);
        assertNotNull("Did not create other payment point", otherPaymentPoint);

        Cashbox cashbox = paymentPointUtil.addCashBox(paymentPoint, "test cash box");
        assertNotNull("Did not create cash box", cashbox);

        operations = CollectionUtils.set();
        operations.add(operationUtil.create(cashbox, 50));
        operations.add(operationUtil.create(cashbox, 741));

        operations.add(operationUtil.create(otherPaymentPoint, 442));
    }

    @After
    public void tearDown() {
        setPaymentCollectorId(systemPaymentCollectorId);

        for (Operation operation : operations) {
            operationUtil.delete(operation);
        }
        operations.clear();

        if (paymentPoint != null) {
            paymentPointUtil.delete(paymentPoint);
        }
        if (otherPaymentPoint != null) {
            paymentPointUtil.delete(otherPaymentPoint);
        }
        if (organization != null) {
            organizationUtil.delete(organization);
        }
        if (otherOrganization != null) {
            organizationUtil.delete(otherOrganization);
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
        assertNotNull("Null collection", paymentPointsListMonitorAction.getPaymentPoints());
        assertEquals("Test payment point do not show on page", 1, paymentPointsListMonitorAction.getPaymentPoints().size());
        PaymentPointMonitorContainer container = paymentPointsListMonitorAction.getPaymentPoints().get(0);
        assertEquals("Test payment point do not show on page", String.valueOf(paymentPoint.getId()), container.getId());
        assertEquals("Payment point total sum is incorrect", "791.00", paymentPointsListMonitorAction.getPaymentPoints().get(0).getTotalSum());
    }

    @Test
    public void testStartAndStopTradingDay() throws Exception {
        // Process
        assertEquals("Action failed", FPActionSupport.SUCCESS, paymentPointsListMonitorAction.execute());
        assertEquals("Number payment point is not 1", 1, paymentPointsListMonitorAction.getPaymentPoints().size());
        PaymentPointMonitorContainer container = paymentPointsListMonitorAction.getPaymentPoints().get(0);
        assertNull("Payment point status is not empty", container.getStatus());
        assertEquals("Payment point action is not enable", paymentPointsListMonitorAction.getText(PaymentPointsListMonitorAction.ENABLE), container.getActionName());

        // Start trading day process
        assertNull("Trading must be not starting", paymentPoint.getTradingDayProcessInstanceId());
        paymentPointsListMonitorAction.setPaymentPointId(String.valueOf(paymentPoint.getId()));
        paymentPointsListMonitorAction.setAction(PaymentPointsListMonitorAction.ENABLE);
        assertEquals("Action failed", FPActionSupport.SUCCESS, paymentPointsListMonitorAction.execute());
        assertEquals("Number payment point is not 1", 1, paymentPointsListMonitorAction.getPaymentPoints().size());
        container = paymentPointsListMonitorAction.getPaymentPoints().get(0);
        assertNotNull("Payment point status is empty", container.getStatus());
        assertEquals("Payment point action is not disable", paymentPointsListMonitorAction.getText(PaymentPointsListMonitorAction.DISABLE), container.getActionName());

        // Check start trading day process
        paymentPoint = paymentPointService.read(Stub.stub(paymentPoint));
        assertNotNull("Payment point deleted", paymentPoint);
        Long processInstanceId = paymentPoint.getTradingDayProcessInstanceId();
        assertNotNull("Payment point`s process instance id is null", processInstanceId);
        Process process = processManager.getProcessInstanceInfo(processInstanceId);
        assertNotNull("Failed start process", process);

        // Stop trading day process
        paymentPointsListMonitorAction.setPaymentPointId(String.valueOf(paymentPoint.getId()));
        paymentPointsListMonitorAction.setAction(PaymentPointsListMonitorAction.DISABLE);
        assertEquals("Action failed", FPActionSupport.SUCCESS, paymentPointsListMonitorAction.execute());
        assertEquals("Number payment point is not 1", 1, paymentPointsListMonitorAction.getPaymentPoints().size());
        container = paymentPointsListMonitorAction.getPaymentPoints().get(0);
        assertNull("Payment point status is not empty", container.getStatus());
        assertEquals("Payment point action is not enable", paymentPointsListMonitorAction.getText(PaymentPointsListMonitorAction.ENABLE), container.getActionName());

        // Check stop trading day process
        paymentPoint = paymentPointService.read(Stub.stub(paymentPoint));
        assertNotNull("Payment point deleted", paymentPoint);
        assertNull("Payment point`s process instance id is not null", paymentPoint.getTradingDayProcessInstanceId());
        process = processManager.getProcessInstanceInfo(processInstanceId);
        assertNull("Trading process did not delete", process);
    }

    private Long getPaymentCollectorId() {
        return ((PaymentsUserPreferences)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getPaymentCollectorId();
    }

    private void setPaymentCollectorId(Long paymentCollectorId) {
        ((PaymentsUserPreferences)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).setPaymentCollectorId(paymentCollectorId);
    }
}
