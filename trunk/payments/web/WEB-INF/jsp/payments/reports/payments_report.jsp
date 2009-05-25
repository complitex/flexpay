<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<script type="text/javascript">

	function validateBeginAfterEnd() {
		var begin = Date.parse($("#beginDateFilter").val() + " " + $("#beginTimeFilter").val());
		var end = Date.parse($("#endDateFilter").val() + " " + $("#endTimeFilter").val());
		if (begin > end) {
			alert("'<s:text name="error.from_after_till_tm"/>'");
			return false;
		}
		return true;
	};
</script>

<s:actionerror />

<form action="<s:url action="generatePaymentsReport" />" onsubmit="return validateBeginAfterEnd();">
	<table>
		<tr>
			<td nowrap="nowrap">
				<s:text name="payments.report.generate.date_from" />
			</td>
			<td nowrap="nowrap">
				<%@ include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>
			</td>
			<td nowrap="nowrap">
				<%@ include file="/WEB-INF/jsp/common/filter/begin_time_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">
				<s:text name="payments.report.generate.date_till" />
			</td>
			<td nowrap="nowrap">
				<%@ include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp" %>
			</td>
			<td nowrap="nowrap">
				<%@ include file="/WEB-INF/jsp/common/filter/end_time_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">
				<input type="submit" name="submitted" class="btn-exit"
					   value="<s:text name="payments.reports.generate.generate"/>"/>
			</td>
		</tr>
	</table>
</form>

<s:if test="%{reportContentIsNotEmpty()}">
	<table cellpadding="3" cellspacing="1" border="0" width="100%" class="operations">

		<tr>
			<td class="th"><s:text name="payments.reports.payment_report.number_doc"/></td>
			<td class="th"><s:text name="payments.reports.payment_report.payment_point"/></td>
			<td class="th"><s:text name="payments.reports.payment_report.number_oper"/></td>
			<td class="th"><s:text name="payments.reports.payment_report.operation_id"/></td>
			<td class="th"><s:text name="payments.reports.payment_report.service_provider_account"/></td>
			<td class="th"><s:text name="payments.reports.payment_report.fio"/></td>
			<td class="th"><s:text name="payments.reports.payment_report.service_type_code"/></td>
			<td class="th"><s:text name="payments.reports.payment_report.document_summ"/></td>
			<td class="th"><s:text name="payments.reports.payment_report.document_id"/></td>
		</tr>

		<%-- document row --%>
		<s:iterator value="reportContent" status="status">
		<tr>
			<td class="cols_1" nowrap="nowrap"><s:property value="%{#status.count}"/></td>
			<td class="cols_1" nowrap="nowrap"><s:property value="paymentPointId"/></td>
			<td class="cols_1" nowrap="nowrap"><s:property value="operationCount"/></td>
			<td class="cols_1" nowrap="nowrap"><s:property value="operationId"/></td>
			<td class="cols_1" nowrap="nowrap"><s:property value="serviceProviderAccount"/></td>
			<td class="cols_1" nowrap="nowrap"><s:property value="fio"/></td>
			<td class="cols_1" nowrap="nowrap"><s:property value="serviceTypeCode"/></td>
			<td class="cols_1" nowrap="nowrap"><s:property value="documentSumm"/></td>
			<td class="cols_1" nowrap="nowrap"><s:property value="documentId"/></td>
		</tr>
		</s:iterator>
	</table>
</s:if>
