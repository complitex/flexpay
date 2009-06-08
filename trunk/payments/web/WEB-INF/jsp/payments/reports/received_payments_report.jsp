<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<script type="text/javascript">
	function printReport() {
		var url = '<s:url action="receivedPaymentsReport" includeParams="none" />';
		url += '?beginDateFilter.stringDate=' + $('#beginDateFilter').val() + '&submitted=submitted';
		window.open(url, "_blank");
	}
</script>

<s:actionerror/>

<s:form action="receivedPaymentsReport">
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
	</table>
</s:form>