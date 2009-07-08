<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<form action="<s:url action="accReceivedPaymentsReport"/>">
	<table>
		<tr>
			<td nowrap="nowrap">
				<s:text name="payments.report.generate.date_from"/>
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
				<s:text name="payments.report.generate.date_till"/>
			</td>
			<td nowrap="nowrap">
				<%@ include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp" %>
			</td>
			<td nowrap="nowrap">
				<%@ include file="/WEB-INF/jsp/common/filter/end_time_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<s:text name="payments.report.acc.returned.details_for"/>

			</td>
		</tr>
	</table>
</form>