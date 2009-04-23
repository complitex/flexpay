<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp"%>

<s:form action="generatePaymentsReport" target="_blank">
	<table>
		<tr>
			<td nowrap="nowrap">
				<s:text name="payments.report.generate.date_from"/>
				<%@ include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp"%>
			</td>
			<td nowrap="nowrap">
				<s:text name="payments.report.generate.date_till"/>
				<%@ include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp"%>
			</td>
			<td nowrap="nowrap">
				<input type="submit" name="submitted" class="btn-exit"
					   value="<s:property value="%{getText('payments.reports.generate.generate')}"/>"/>
			</td>
		</tr>
	</table>
</s:form>
