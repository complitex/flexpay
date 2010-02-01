<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<script type="text/javascript">
	function printReport() {
		var url = '<s:url action="dayReceivedPaymentsReport" includeParams="none" />';
		url += '?beginDateFilter.stringDate=' + $('#beginDateFilter').val() +
			   '&beginTimeFilter.stringDate=' + $('#beginTimeFilter').val() +
			   '&endTimeFilter.stringDate=' + $('#endTimeFilter').val() +
			   '&showDetails=' + $('#showDetails').attr('checked') +  '&submitted=submitted';
		window.open(url, "_blank");
	}
</script>

<s:actionerror/>

<s:form action="dayReceivedPaymentsReport">
	<table>
		<tr>
			<td nowrap="nowrap">
				<s:text name="payments.report.generate.date"/>
			</td>
			<td nowrap="nowrap">
				<%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>
			</td>
		</tr>

		<tr>
			<td nowrap="nowrap">
				<s:text name="payments.report.generate.time_from"/>
			</td>
			<td nowrap="nowrap">
				<%@ include file="/WEB-INF/jsp/common/filter/begin_time_filter.jsp" %>
			</td>
		</tr>

		<tr>
			<td nowrap="nowrap">
				<s:text name="payments.report.generate.time_till"/>
			</td>
			<td nowrap="nowrap">
				<%@ include file="/WEB-INF/jsp/common/filter/end_time_filter.jsp" %>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<s:text name="payments.report.generate.show_details"/>
				<s:checkbox id="showDetails" name="showDetails" value="true"/>
			</td>
		</tr>

		<tr>
			<td nowrap="nowrap" colspan="2">
				<input type="button" name="submitted" class="btn-exit" onclick="printReport();"
					   value="<s:text name="payments.reports.generate.generate"/>"/>
			</td>
		</tr>

	</table>
</s:form>