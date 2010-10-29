<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%" class="cashboxes_list">
    <tr>
        <td colspan="6">
            <%@include file="/WEB-INF/jsp/payments/trading_day/trading_day_control_panel.jsp"%>
        </td>
    </tr>
    <tr>
        <td colspan="6">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="payments.payment_point.detail.cashboxes.list.cashbox" /></td>
        <td class="th"><s:text name="payments.payment_point.detail.cashboxes.list.status" /></td>
        <td class="th"><s:text name="payments.payment_point.detail.cashboxes.list.sum" /></td>
        <td class="th"><s:text name="payments.payment_point.detail.cashboxes.list.FIO_cashier" /></td>
        <td class="th"><s:text name="payments.payment_point.detail.cashboxes.list.last_payment" /></td>
        <td class="th"><s:text name="payments.payment_point.detail.cashboxes.list.payments_count" /></td>
    </tr>
    <s:iterator value="cashboxes">
        <tr>
            <td><s:property value="#status.index + pager.thisPageFirstElementNumber + 1" /></td>
            <td>
                <a href="<s:url action="operationsListMonitor" includeParams="none"><s:param name="cashbox.id" value="id" /></s:url>">
                    <s:property value="cashbox" />
                </a>
            </td>
            <td><s:property value="status" /></td>
            <td><s:property value="totalSum" /></td>
            <td><s:property value="cashierFIO" /></td>
            <td><s:property value="lastPayment" /></td>
            <td><s:property value="paymentsCount" /></td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="6">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
</table>
