<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/stylesheet.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>


<table cellpadding="3" cellspacing="1" border="0" width="100%" class="payment_points_list">
    <tr>
        <td colspan="9">
            <%@include file="/WEB-INF/jsp/payments/trading_day/trading_day_control_panel.jsp"%>
        </td>
    </tr>
    <tr>
        <td colspan="9">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="payments.payment_points.list.payment_point" /></td>
        <td class="th"><s:text name="payments.payment_points.list.payments_count" /></td>
        <td class="th"><s:text name="payments.payment_points.list.payment_point_status" /></td>
        <td class="th"><s:text name="payments.payment_points.list.payment_point_sum" /></td>
        <td class="th"><s:text name="payments.payment_points.list.cashbox" /></td>
        <td class="th"><s:text name="payments.payment_points.list.FIO_cashier" /></td>
        <td class="th"><s:text name="payments.payment_points.list.last_payment" /></td>
    </tr>
    <s:iterator value="paymentPoints" status="iterStatus">
        <tr>
            <td><s:property value="#iterStatus.index + pager.thisPageFirstElementNumber + 1" /></td>
            <td>
                <a href="<s:url action="paymentPointDetailMonitor"><s:param name="paymentPoint.id" value="id" /></s:url>">
                    <s:property value="name" />
                </a>
            </td>
            <td><s:property value="paymentsCount" /></td>
            <td><s:property value="status" /></td>
            <td><s:property value="totalSum" /></td>
            <td><s:property value="cashbox" /></td>
            <td><s:property value="cashierFIO" /></td>
            <td><s:property value="lastPayment" /></td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="9">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
</table>
