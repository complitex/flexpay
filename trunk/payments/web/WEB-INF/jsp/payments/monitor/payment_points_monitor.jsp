<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/stylesheet.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
<%--
    <tr>
        <td>
            <s:text name="payments.payment_points.list.filter"/>
            <input type="text" name="filter"/>
            <input type="submit" name="submitFilter" value="<s:text name="payments.payment_points.list.filter.submit"/>"/>
        </td>
    </tr>
--%>
    <tr>
        <td><s:text name="payments.payment_points.list.updated" />&nbsp;<s:property value="updated" /></td>
        <td align="left">
            <input type="button" value="<s:text name="payments.payment_points.list.update" />" onclick="window.location='<s:url action="paymentPointsListMonitor" includeParams="none" />';" />
        </td>
    </tr>
</table>

<span id="result"></span>

<script type="text/javascript">

    $(function() {
        pagerAjax();
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="paymentPointsListMonitorAjax" includeParams="none" />"
        });
    }

    function enableDisablePaymentPoint(id, action) {
        FP.serviceElements("<s:url action="paymentPointEnableDisable" includeParams="none" />", null, pagerAjax,
                            {params:{
                                "paymentPoint.id":id,
                                action:action
                            }});
    }

</script>
