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
					   value="<s:property value="%{getText('payments.reports.generate.generate')}"/>" />
			</td>
		</tr>
	</table>
</form>
