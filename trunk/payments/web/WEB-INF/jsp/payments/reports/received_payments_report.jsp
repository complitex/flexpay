<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<s:actionerror/>

<s:form action="receivedPaymentsReport">
	<table>
		<tr>
			<td nowrap="nowrap" style="padding-right: 50px;">
				<s:text name="payments.report.received.organization"/>
				<s:select name="reportOrganizationId" list="organizations" listKey="id" listValue="name"/>
			</td>
			<td nowrap="nowrap">
				<s:text name="payments.report.generate.date"/>
				<%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>
			</td>
			<td nowrap="nowrap">
				<input type="submit" name="submitted" class="btn-exit" value="<s:text name="payments.reports.generate.generate"/>"/>				
			</td>
		</tr>
	</table>
</s:form>

<s:if test="%{operationsAreEmpty() && isSubmit()}">
	<s:text name="payments.reports.received.no_received_payments"/>
</s:if>

<s:elseif test="%{!operationsAreEmpty()}">
	<table cellpadding="3" cellspacing="1" border="0" width="100%" class="operations">
		<tr>
			<td class="th" width="1%"><s:text name="payments.reports.received.number_symbol"/></td>
			<td class="th" width="1%"><s:text name="payments.reports.received.uno"/></td>
			<td class="th"><s:text name="payments.reports.received.creation_date"/></td>
			<td class="th"><s:text name="payments.reports.received.address"/></td>
			<td class="th"><s:text name="payments.reports.received.fio"/></td>
			<td class="th"><s:text name="payments.reports.received.summ"/></td>
			<td class="th"><s:text name="payments.reports.received.pay_summ"/></td>
			<td class="th"><s:text name="payments.reports.received.change"/></td>
			<td class="th"><s:text name="payments.reports.received.service"/></td>
			<td class="th"><s:text name="payments.reports.received.provider"/></td>
		</tr>

		<%-- operation rows --%>
		<s:iterator value="operations" status="opStatus">
			<%-- operation header  --%>
			<tr valign="middle">
				<td class="col_oper" align="right"><s:property value="%{#opStatus.index + 1}"/></td>
				<td class="col_oper" nowrap="nowrap"><s:property value="id"/></td>
				<td class="col_oper" nowrap="nowrap"><s:date name="creationDate" format="HH:mm"/></td>
				<td class="col_oper" nowrap="nowrap">&nbsp;</td>
				<td class="col_oper" nowrap="nowrap">&nbsp;</td>
				<td class="col_oper" nowrap="nowrap">&nbsp;</td>
				<td class="col_oper" nowrap="nowrap">&nbsp;</td>
				<td class="col_oper" nowrap="nowrap">&nbsp;</td>
				<td class="col_oper" nowrap="nowrap">&nbsp;</td>
				<td class="col_oper" nowrap="nowrap">&nbsp;</td>
			</tr>

			<%-- document rows --%>
			<s:iterator value="documents">
				<s:if test="%{!isDocumentDeleted(documentStatus.code)}">
					<tr>
						<td class="col_doc" nowrap="nowrap">&nbsp;</td>
						<td class="col_doc" nowrap="nowrap">&nbsp;</td>
						<td class="col_doc" nowrap="nowrap">&nbsp;</td>
						<td class="col_doc" nowrap="nowrap"><s:property value="address"/></td>
						<td class="col_doc" nowrap="nowrap"><s:property value="payerFIO"/></td>
						<td class="col_doc" nowrap="nowrap"><s:property value="summ"/></td>
						<td class="col_doc" nowrap="nowrap">&nbsp;</td>
						<td class="col_doc" nowrap="nowrap">&nbsp;</td>
						<td class="col_doc" nowrap="nowrap"><s:property value="%{getServiceTypeName(service)}"/></td>
						<td class="col_doc" nowrap="nowrap"><s:property value="%{getServiceProviderName(service)}"/></td>
					</tr>
				</s:if>
			</s:iterator>

			<%-- operation footer --%>
			<tr valign="middle">
				<td class="col_oper" nowrap="nowrap">&nbsp;</td>
				<td class="col_oper" nowrap="nowrap">&nbsp;</td>
				<td class="col_oper" nowrap="nowrap">&nbsp;</td>
				<td class="col_oper" nowrap="nowrap"><s:property value="address"/></td>
				<td class="col_oper" nowrap="nowrap"><s:property value="payerFIO"/></td>
				<td class="col_oper" nowrap="nowrap"><s:property value="operationSumm"/></td>
				<td class="col_oper" nowrap="nowrap"><s:property value="operationInputSumm"/></td>
				<td class="col_oper" nowrap="nowrap"><s:property value="change"/></td>
				<td class="col_oper" nowrap="nowrap">&nbsp;</td>
				<td class="col_oper" nowrap="nowrap">&nbsp;</td>
			</tr>
		</s:iterator>

		<tr>
			<td class="th" colspan="10">
				<s:text name="payments.reports.received.totals">
					<s:param value="paymentsCount" />
					<s:param value="paymentsSumm" />
					<s:param value="returnsCount" />
					<s:param value="returnsSumm" />
				</s:text>
			</td>
		</tr>
	</table>
</s:elseif>
