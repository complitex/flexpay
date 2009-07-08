<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<script type="text/javascript">
	function printReport() {
		var url = '<s:url action="dayReceivedPaymentsReport" includeParams="none" />';
		url += '?beginDateFilter.stringDate=' + $('#beginDateFilter').val() +
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
				<%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>
			</td>
			<td nowrap="nowrap">
				<input type="button" name="submitted" class="btn-exit" onclick="printReport();"
					   value="<s:text name="payments.reports.generate.generate"/>"/>
			</td>
		</tr>
		<tr>
			<td>
				<s:text name="payments.report.generate.show_details"/>
				<s:checkbox id="showDetails" name="showDetails" value="true"/>
			</td>
		</tr>
	</table>
</s:form>