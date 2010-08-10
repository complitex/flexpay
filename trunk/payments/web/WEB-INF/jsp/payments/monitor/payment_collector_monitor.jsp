<%@ page import="org.flexpay.payments.service.Roles" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/stylesheet.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">

    $("#result").ready(function() {
        pagerAjax();
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="paymentPointsListMonitorAjax" includeParams="none" />"
        });
    }

    function enableDisablePaymentCollector(action) {
        FP.serviceElements("<s:url action="paymentCollectorEnableDisable" includeParams="none" />", null, updatePage,
                            {params:{action:action}});
    }

    $("#updated").ready(function() {
        updatePage();
    });

    function updatePage() {

        var $status = $("#status");
        var $actionBut = $("#actionBut");
        var $updated = $("#updated");

        $updated.html("<img src=\"<s:url value="/resources/common/img/indicator.gif" />\" />");
        $status.html("<img src=\"<s:url value="/resources/common/img/indicator.gif" />\" />");
        $actionBut.css({display:"none"});

        $.getJSON("<s:url action="paymentCollectorMonitorData" includeParams="none" />", {},
            function(data) {
                $updated.text(data.updated);
                $status.text(data.status);
                $actionBut.val(data.actionText).css({display:"block"}).click(function() {
                    enableDisablePaymentCollector(data.action);
                });
            });
        pagerAjax();
    }

</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="2">
            <s:text name="payments.payment_points.list.payment_collector" />:&nbsp;<s:property value="paymentCollector.getName(getLocale())" />
        </td>
    </tr>
    <tr>
        <td>
            <s:text name="payments.payment_points.list.trading_day_status" />:&nbsp;<span id="status"></span>
        </td>
        <sec:authorize ifAnyGranted="<%=Roles.TRADING_DAY_ADMIN_ACTION%>">
            <td align="left">
                <input id="actionBut" type="button" value="<s:text name="%{action}" />" />
            </td>
        </sec:authorize>
    </tr>
    <tr>
        <td><s:text name="payments.payment_points.list.last_update" />:&nbsp;<span id="updated"></span></td>
        <td align="left">
            <input type="button" value="<s:text name="payments.payment_points.list.update" />" onclick="updatePage();" />
        </td>
    </tr>
</table>

<span id="result"></span>
