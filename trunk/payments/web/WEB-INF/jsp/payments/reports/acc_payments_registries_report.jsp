<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/stylesheet.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bbq.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table>
    <tr>
        <td>
            <s:text name="payments.report.generate.date_from" />
            <%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp"%>
            <%@include file="/WEB-INF/jsp/common/filter/begin_time_with_sec_filter.jsp"%>
        </td>
    </tr>
    <tr>
        <td>
            <s:text name="payments.report.generate.date_till" />
            <%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp"%>
            <%@include file="/WEB-INF/jsp/common/filter/end_time_with_sec_filter.jsp"%>
        </td>
    </tr>
    <tr>
        <td>
            <s:text name="payments.report.acc.payments_registries.details_for" />
            <%@include file="/WEB-INF/jsp/payments/filters/service_provider_filter.jsp"%>
        </td>
    </tr>
    <tr>
        <td>
            <input id="print" type="button" class="btn-exit" onclick="printReport('pdf');" value="<s:text name="payments.reports.generate.generate" />" />
        </td>
    </tr>
</table>

<%--
<div id="dialog" style="display:none;">
    <s:text name="payments.reports.choose_print_format_question" /><br />
    <input type="button" class="btn-exit" onclick="printReport('html');" value="<s:text name="payments.reports.html_format" />" />
    <input type="button" class="btn-exit" onclick="printReport('pdf');" value="<s:text name="payments.reports.pdf_format" />" />
</div>
--%>

<script type="text/javascript">

/*
    $(function() {
        $("#print").click(function (e) {
            e.preventDefault();
            $("#dialog").modal();
        });
    });
*/

    function printReport(format) {
//        $.modal.close();
        <s:if test="userPreferences.paymentCollectorId == ''">
            alert("<s:text name="payments.error.payment_collector_not_found" />");
        </s:if><s:else>
            var params = {
                "beginDateFilter.stringDate":$("#beginDateFilter").val(),
                "beginTimeFilter.stringDate":$("#beginTimeFilter").val(),
                "endDateFilter.stringDate":$("#endDateFilter").val(),
                "endTimeFilter.stringDate":$("#endTimeFilter").val(),
                "paymentCollectorFilter.selectedId":$("paymentCollectorFilter.selectedId").val(),
                format:format,
                submitted:"true"
            };
            window.open($.param.querystring("<s:url action="accPaymentsRegistriesReport" />", params), "_blank");
        </s:else>
    }

</script>
