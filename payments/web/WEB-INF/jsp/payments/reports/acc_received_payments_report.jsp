<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/stylesheet.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bbq.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="accReceivedPaymentsReport" method="POST">
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
                <s:text name="payments.report.acc.received.details_for" />
                <select id="details" name="details" class="form-select" onchange="updateControls();">
                    <option value="1"<s:if test="details == 1"> selected</s:if>>
                        <s:text name="payments.report.acc.received.details_payment_point" />
                    </option>
                    <option value="2"<s:if test="details == 2"> selected</s:if>>
                        <s:text name="payments.report.acc.received.details_cashbox" />
                    </option>
                    <option value="3"<s:if test="details == 3"> selected</s:if>>
                        <s:text name="payments.report.acc.received.details_payments" />
                    </option>
                </select>
            </td>
		</tr>
		<tr>
			<td>
				<s:text name="payments.report.acc.received.payment_point" />
				<%@include file="/WEB-INF/jsp/orgs/filters/payment_points_filter.jsp"%>
			</td>
		</tr>
		<tr>
			<td>
				<s:text name="payments.report.acc.received.cashbox" />
				<%@include file="/WEB-INF/jsp/orgs/filters/cashbox_filter.jsp"%>
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

    $(function() {
        updateControls();

/*
        $("#print").click(function (e) {
            e.preventDefault();
            $("#dialog").modal();
        });

*/
    });

    function updateControls() {
        FP.endis("cashboxFilter.selectedId", $("#details").val() != 1);
    }

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
                "paymentPointsFilter.selectedId":$("paymentPointsFilter.selectedId").val(),
                "cashboxFilter.selectedId":$("cashboxFilter.selectedId").val(),
                details:$("#details").val(),
                format:format,
                submitted:"true"
            };
            window.open($.param.querystring("<s:url action="accReceivedPaymentsReport" includeParams="none" />", params), "_blank");
        </s:else>
    }

</script>
