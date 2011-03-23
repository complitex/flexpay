<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_timeentry.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bbq.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/stylesheet.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/flexpay_operations.jsp"%>

<s:hidden name="documentSearch" />

<table cellpadding="3" cellspacing="1" border="0" width="100%" class="operations">
    <sec:authorize ifAllGranted="ROLE_PAYMENTS_DEVELOPER">
        <tr>
            <td><s:text name="payments.report.generate.date_from" /></td>
            <td><%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp"%></td>
            <td><s:text name="payments.report.generate.date_till" /></td>
            <td><%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp"%></td>
        </tr>
    </sec:authorize>
    <tr>
        <td><s:text name="payments.operations.list.time_from"/></td>
        <td><%@include file="/WEB-INF/jsp/common/filter/begin_time_with_sec_filter.jsp"%></td>
        <td><s:text name="payments.operations.list.time_till"/></td>
        <td><%@include file="/WEB-INF/jsp/common/filter/end_time_with_sec_filter.jsp"%></td>
    </tr>
    <tr>
        <td><s:text name="payments.operations.list.sum_from" /></td>
        <td><s:textfield name="minimalSum" /></td>
        <td><s:text name="payments.operations.list.sum_up_to" /></td>
        <td><s:textfield name="maximalSum" /></td>
    </tr>
    <tr>
        <td><s:text name="payments.operations.list.service_type" /></td>
        <td colspan="2"><%@include file="/WEB-INF/jsp/payments/filters/service_type_filter.jsp"%></td>
        <td>
            <input type="button" class="btn-exit" value="<s:text name="payments.operations.list.filter" />" onclick="pagerAjax();" />
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
            action:"<s:url action="operationsListAjax"/>",
            params: {
                "cashbox.id":<s:property value="cashbox.id" />,
                <sec:authorize ifAllGranted="ROLE_PAYMENTS_DEVELOPER">
                    "beginDateFilter.stringDate":$("input[name='beginDateFilter.stringDate']").val(),
                    "endDateFilter.stringDate":$("input[name='endDateFilter.stringDate']").val(),
                </sec:authorize>
                "beginTimeFilter.stringDate":$("input[name='beginTimeFilter.stringDate']").val(),
                "endTimeFilter.stringDate":$("input[name='endTimeFilter.stringDate']").val(),
                "serviceTypeFilter.selectedId":$("select[name='serviceTypeFilter.selectedId']").val(),
                "operationSorterById.active": $("#operationSorterByIdActive").val(),
                "operationSorterById.order": $("#operationSorterByIdOrder").val(),
                "minimalSumFilter.value":$("input[name='minimalSum']").val(),
                "maximalSumFilter.value":$("input[name='maximalSum']").val(),
                documentSearch:$("input[name='documentSearch']").val(),
            }
        });
    }

    function sorterAjax() {
        pagerAjax();
    }

    function setStatus(status) {
        FP.serviceElements("<s:url action="operationSetStatus" />", "operation.id", pagerAjax,
                            {params:{status:status}});
    }

    function printQuittance(opId) {
        var params = {
            copy: true,
            operationId: opId,
            format:"pdf"
        };
        window.open($.param.querystring("<s:url action="paymentOperationReportAction" />", params), "_blank");
    }

</script>
