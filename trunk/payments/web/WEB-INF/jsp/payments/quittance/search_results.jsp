<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%@include file="search_results_validation.jsp" %>

<s:actionerror/>

<s:if test="%{resultsAreNotEmpty()}">
	<form id='quittancePayForm' action="<s:url action="paymentsQuittancePay" />">
		<table class="search_results" cellpadding="3" cellspacing="1" border="0" width="100%">
			<tr>
				<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.consumer_account"/></td>
				<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.fio"/></td>
				<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.service"/></td>
				<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.service_supplier"/></td>
				<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.debt"/></td>
				<td class="th" nowrap="nowrap" style="width: 80px;"><s:text name="payments.quittances.quittance_pay.pay"/></td>
			</tr>

			<s:iterator value="quittanceInfos" id="qi" status="nQI">
				<s:iterator value="detailses" status="status">
					<s:set name="serviceId" value="%{getServiceId(serviceMasterIndex)}"/>
					<s:set name="serviceIndx" value="%{getServiceFullIndex(#nQI.index, #serviceId)}"/>

					<s:hidden name="payerFios['%{#serviceIndx}']" value="%{getPersonFio(#qi)}"/>
					<s:hidden name="addresses['%{#serviceIndx}']" value="%{getApartmentAddress(#qi)}"/>
					<s:hidden name="eircAccounts['%{#serviceIndx}']" value="%{getEircAccount(#qi)}"/>
					<s:hidden name="serviceProviderAccounts['%{#serviceIndx}']" value="%{serviceProviderAccount}"/>

					<tr class="cols_1_error" style="display:none;">
						<td colspan="6"/>
					</tr>

					<tr class="cols_1 service_payment">
						<td class="col" nowrap="nowrap"><s:property value="%{serviceProviderAccount}"/></td>
						<td class="col" nowrap="nowrap"><s:property value="%{getPersonFio(#qi)}"/></td>
						<td class="col" nowrap="nowrap"><s:property value="%{getServiceName(serviceMasterIndex)}"/></td>
						<td class="col"><s:property value="%{getProviderName(serviceMasterIndex)}"/></td>
						<td class="col"><s:property value="outgoingBalance"/></td>
						<td class="col"><s:textfield name="payments['%{#serviceIndx}']"
													 id="payments_%{#serviceIndx}"
													 value="%{outgoingBalance}"
													 onkeypress="return FP.disableEnterKey(event);"
													 onblur="replaceEmptyValueWithZero('payments_%{#serviceIndx}');"
													 cssStyle="width: 100%; text-align: right;"/></td>
					</tr>
				</s:iterator>
			</s:iterator>

			<tr class="cols_1_error" style="display:none;">
				<td colspan="6"/>
			</tr>
			<tr class="cols_1">
				<td class="col" colspan="5" style="text-align:right;font-weight:bold;">
					<s:text name="payments.quittances.quittance_pay.total_payable"/></td>
				<td class="col"><s:textfield name="totalToPay" readonly="true" value="%{getTotalToPay()}"
											 onkeypress="return FP.disableEnterKey(event);"
											 cssStyle="width: 100%; text-align: right;"/></td>
			</tr>

			<tr class="cols_1_error" style="display:none;">
				<td colspan="6"/>
			</tr>
			<tr class="cols_1">
				<td colspan="5" style="font-weight: bold; text-align: right; "><s:text
						name="payments.quittance.payment.input"/></td>
				<td><s:textfield name="inputSumm" cssStyle="width: 100%; text-align: right;"
								 value="%{getTotalToPay()}"
								 onkeypress="return FP.disableEnterKey(event);"/></td>
			</tr>

			<tr class="cols_1">
				<td colspan="5" style="font-weight: bold; text-align: right;"><s:text
						name="payments.quittance.payment.change"/></td>
				<td><s:textfield name="changeSumm" cssStyle="width: 100%; text-align: right;" value="0.00"
								 onkeypress="return FP.disableEnterKey(event);" readonly="true"/></td>
			</tr>

			<tr>
				<td colspan="5" style="text-align:left;"/>
				<td style="text-align:right;">
					<s:hidden name="actionName" value="%{actionName}"/>
					<input type="submit" name="submitted" class="btn-exit" style="width: 100%;"
						   value="<s:text name="payments.quittances.quittance_pay.pay"/>"/>
				</td>
			</tr>

		</table>
	</form>
</s:if>
<s:else>
	<s:text name="payments.quittances.quittance_pay.no_debts_found"/>
</s:else>
