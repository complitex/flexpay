<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/stylesheet.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td><s:text name="payments.payment_points.list.updated" />&nbsp;<s:property value="updated" /></td>
        <td align="left">
            <input type="button" value="<s:text name="payments.payment_points.list.update" />" onclick="window.location='<s:url action="paymentPointDetailMonitor" includeParams="none"><s:param name="paymentPoint.id" value="paymentPoint.id" /></s:url>';" />
        </td>
    </tr>
    <tr>
        <td colspan="2"><s:text name="payments.payment_point" />&nbsp;:&nbsp;<s:property value="name" /></td>
    </tr>
    <tr>
        <td colspan="2">
            <s:text name="payments.payment_point.detail.payments_count" />&nbsp;:&nbsp;<s:property value="paymentsCount" />&nbsp;&nbsp;&nbsp;
            <s:text name="payments.payment_point.detail.sum" />&nbsp;:&nbsp;<s:property value="totalSumm" />
        </td>
    </tr>
<%--
    <tr>
        <td colspan="2"><s:text name="payments.payment_point.detail.status" />: <s:property value="status" /></td>
    </tr>
    <tr>
        <td colspan="2">
            <s:iterator value="buttons" id="button">
                <input type="button" name="activity" value="<s:property value="button" />" />
            </s:iterator>
        </td>
    </tr>
--%>
</table>

<span id="result"></span>

<script type="text/javascript">

    $(function() {
        pagerAjax();
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="paymentPointCashboxesListAjax" includeParams="none"/>",
            params: {
                "paymentPoint.id":<s:property value="paymentPoint.id" />
            }
        });
    }

</script>
