<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp"%>

<script type="text/javascript">
	FP.calendars("#beginDate", true);
	FP.calendars("#endDate", true);

	function showDownloadLink() {
		$('#downloadLinkDiv').show();
		return false;
	}
</script>

<s:form>
	<table>
		<tr>
			<td nowrap="nowrap">
				<s:text name="payments.report.generate.date_from"/>
				<s:textfield name="beginDate" id="beginDate" readonly="true"/>
			</td>
			<td nowrap="nowrap">
				<s:text name="payments.report.generate.date_till"/>
				<s:textfield name="endDate" id="endDate" readonly="true"/>
			</td>
			<td nowrap="nowrap">
				<input type="button" name="submitted" class="btn-exit"
					   value="<s:property value="%{getText('payments.reports.generate.generate')}"/>"
					   onclick="showDownloadLink();"/>
			</td>
		</tr>
	</table>
</s:form>

<div id="downloadLinkDiv" style="display: none;">
	<s:text name="payments.reports.generate.successfully_generated"/>
	<a href="<s:url value="/resources/payments/demo/report.pdf"/>"><s:text
			name="payments.reports.generate.download"/></a>
</div>