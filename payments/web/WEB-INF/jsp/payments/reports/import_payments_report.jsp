<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/stylesheet.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">

	function validateBeginAfterEnd() {
		var begin = Date.parse($("#beginDateFilter").val() + " " + $("#beginTimeFilter").val());
		var end = Date.parse($("#endDateFilter").val() + " " + $("#endTimeFilter").val());
		if (begin > end) {
			alert("<s:text name="payments.error.from_after_till_tm" />");
			return false;
		}
		return true;
	}
</script>

<s:form action="generatePaymentsReport" onsubmit="return validateBeginAfterEnd();" method="POST">
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
				<input type="submit" name="submitted" class="btn-exit" value="<s:text name="payments.reports.generate.generate" />" />
			</td>
		</tr>
	</table>
</s:form>

<s:if test="reportContentIsNotEmpty()">
	<table cellpadding="3" cellspacing="1" border="0" width="100%" class="operations">
		<tr>
			<td class="th"><s:text name="payments.reports.payment_report.number_doc" /></td>
			<td class="th"><s:text name="payments.reports.payment_report.payment_point" /></td>
			<td class="th"><s:text name="payments.reports.payment_report.number_oper" /></td>
			<td class="th"><s:text name="payments.reports.payment_report.operation_id" /></td>
			<td class="th"><s:text name="payments.reports.payment_report.service_provider_account" /></td>
			<td class="th"><s:text name="payments.reports.payment_report.fio" /></td>
			<td class="th"><s:text name="payments.reports.payment_report.service_type_code" /></td>
			<td class="th"><s:text name="payments.reports.payment_report.document_sum" /></td>
			<td class="th"><s:text name="payments.reports.payment_report.document_id" /></td>
		</tr>
		<s:iterator value="reportContent" status="status">
            <tr>
                <td class="cols_1"><s:property value="#status.count" /></td>
                <td class="cols_1"><s:property value="paymentPointId" /></td>
                <td class="cols_1"><s:property value="operationCount" /></td>
                <td class="cols_1"><s:property value="operationId" /></td>
                <td class="cols_1"><s:property value="serviceProviderAccount" /></td>
                <td class="cols_1"><s:property value="fio" /></td>
                <td class="cols_1"><s:property value="serviceTypeCode" /></td>
                <td class="cols_1"><s:property value="documentSum" /></td>
                <td class="cols_1"><s:property value="documentId" /></td>
            </tr>
		</s:iterator>
	</table>
</s:if>
