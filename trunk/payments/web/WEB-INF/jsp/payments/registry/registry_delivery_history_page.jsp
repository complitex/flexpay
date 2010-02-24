<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <s:text name="payments.registry.delivery_history.date_from" />&nbsp;
        <%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp"%>&nbsp;
        <s:text name="payments.registry.delivery_history.date_till" />&nbsp;
        <%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp"%>&nbsp;
        <input type="button" value="<s:text name="payments.registry.delivery_history.filter" />" class="btn-exit" onclick="pagerAjax();" />
    </tr>
    <tr>
        <td id="result">
        </td>
    </tr>
</table>

<script type="text/javascript">

    $(function() {
        pagerAjax();
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="registryDeliveryHistoryAjax" namespace="/payments" includeParams="none" />",
            params: {
                "beginDateFilter.stringDate":$("input[name=beginDateFilter.stringDate]").get(0).value,
                "endDateFilter.stringDate":$("input[name=endDateFilter.stringDate]").get(0).value
            }
        });
    }

    function sendAjax() {
        FP.serviceElements("<s:url action="registryDeliveryHistorySend" includeParams="none" />", "objectIds", pagerAjax);
    }

</script>
