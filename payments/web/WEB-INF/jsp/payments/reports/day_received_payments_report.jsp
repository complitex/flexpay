<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/stylesheet.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_simplemodal.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bbq.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="dayReceivedPaymentsReport" method="POST">
	<table>
    <table>
		<tr>
            <td>
                <s:text name="payments.report.generate.date" />
                <%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp"%>
			</td>
		</tr>
		<tr>
			<td>
				<s:text name="payments.report.generate.time_from" />
				<%@include file="/WEB-INF/jsp/common/filter/begin_time_with_sec_filter.jsp"%>
			</td>
		</tr>
		<tr>
			<td>
                <s:text name="payments.report.generate.time_till" />
                <%@include file="/WEB-INF/jsp/common/filter/end_time_with_sec_filter.jsp"%>
			</td>
		</tr>
		<tr>
			<td>
				<s:text name="payments.report.generate.show_details" />
				<s:checkbox id="showDetails" name="showDetails" value="true" />
			</td>
		</tr>
		<tr>
			<td>
                <input id="print" type="button" class="btn-exit" onclick="printReport('pdf');" value="<s:text name="payments.reports.generate.generate" />" />
			</td>
		</tr>
	</table>
</s:form>

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
        var params = {
            "beginDateFilter.stringDate":$("#beginDateFilter").val(),
            "beginTimeFilter.stringDate":$("#beginTimeFilter").val(),
            "endTimeFilter.stringDate":$("#endTimeFilter").val(),
            showDetails:$("#showDetails").attr("checked"),
            format:format,
            submitted:"true"
        };
        window.open($.param.querystring("<s:url action="dayReceivedPaymentsReport" />", params), "_blank");
    }

</script>
