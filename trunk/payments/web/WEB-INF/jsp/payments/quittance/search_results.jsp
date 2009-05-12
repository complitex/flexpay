<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%@include file="search_results_validation.jsp" %>

<s:actionerror />

<s:if test="%{resultsAreNotEmpty()}">
	<s:iterator value="quittanceInfos" id="qi" status="nQI">
		<form id='quittancePayForm<s:property value="%{#nQI.index}"/>' action="<s:url action="paymentsQuittancePay" />">
		<table cellpadding="3" cellspacing="1" border="0" width="100%">
				<tr>
					<td colspan="5"><s:property value="%{getPersonFio(#qi)}" /></td>
				</tr>

				<s:if test="%{actionName == 'searchByEircAccount' || actionName == 'searchByQuittanceNumber'}">
					<tr>
						<td colspan="5"><s:property value="%{getApartmentAddress(#qi)}" /></td>
					</tr>
				</s:if>

				<tr>
					<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.service" /></td>
					<td class="th" nowrap="nowrap"><s:text
							name="payments.quittances.quittance_pay.service_supplier" /></td>
					<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.payed" /></td>
					<td class="th" nowrap="nowrap" style="width: 80px;"><s:text
							name="payments.quittances.quittance_pay.pay" /></td>
				</tr>

				<s:hidden name="payerFio" value="%{getPersonFio(#qi)}" />
				<s:hidden name="address" value="%{getApartmentAddress(#qi)}" />
				<s:hidden name="eircAccount" value="%{getEircAccount(#qi)}" />

				<s:iterator value="detailses" status="status">
					<s:set name="serviceId" value="%{getServiceId(serviceMasterIndex)}" />
					<s:hidden name="serviceProviderAccountsMap[%{#serviceId}]" value="%{serviceProviderAccount}" />

					<tr class="cols_1_error" style="display:none;">
						<td colspan="4" />
					</tr>

					<tr class="cols_1 service_payment">
							<%--<script type="text/javascript">--%>
							<%--DETAILS.push(new QD("<s:property value="%{#status.index + 1}"/>", "<s:property value="#serviceId"/>", "<s:property value="%{getServiceName(serviceMasterIndex)}"/>", "<s:property value="%{getProviderName(serviceMasterIndex)}"/>", "<s:property value="outgoingBalance"/>", "<s:property value="payed"/>"));--%>
							<%--</script>--%>

						<td class="col" nowrap="nowrap"><s:property
								value="%{getServiceName(serviceMasterIndex)}" /></td>
						<td class="col"><s:property value="%{getProviderName(serviceMasterIndex)}" /></td>
						<td class="col"><s:property value="payed" /></td>
						<td class="col"><s:textfield name="paymentsMap[%{#serviceId}]" value="%{outgoingBalance}"
													 onchange="onPaymentUpdate(this.form)"
													 cssStyle="width: 100%; text-align: right;" /></td>
					</tr>
				</s:iterator>

				<tr class="cols_1_error" style="display:none;">
					<td colspan="4" />
				</tr>
				<tr class="cols_1">
					<td class="col" colspan="3" style="text-align:right;font-weight:bold;"><s:text
							name="payments.quittances.quittance_pay.total_payable" /></td>
					<td class="col"><s:textfield name="totalToPay" readonly="true" value="%{totalToPay}"
												 cssStyle="width: 100%; text-align: right;" /></td>
				</tr>

				<tr class="cols_1_error" style="display:none;">
					<td colspan="4" />
				</tr>
				<tr class="cols_1">
					<td colspan="3" style="font-weight: bold; text-align: right; "><s:text
							name="payments.quittance.payment.input" /></td>
					<td><s:textfield name="inputSumm" cssStyle="width: 100%; text-align: right;"
									 onchange="onInputUpdate(this.form)"
									 value="%{totalToPay}" /></td>
				</tr>

				<tr class="cols_1">
					<td colspan="3" style="font-weight: bold; text-align: right;"><s:text
							name="payments.quittance.payment.change" /></td>
					<td><s:textfield name="changeSumm" cssStyle="width: 100%; text-align: right;" value="0.00"
									 readonly="true" /></td>
				</tr>

				<tr>
					<td colspan="3" style="text-align:left;">
							<%--<input type="button" value="<s:text name="payments.quittance.payment.pay_asc"/>" class="btn-exit" onclick="divideAscending();"/>--%>
							<%--<input type="button" value="<s:text name="payments.quittance.payment.pay_by_ratio"/>" class="btn-exit" onclick="divideByRatio();"/>--%>
					</td>
					<td colspan="1" style="text-align:right;">
						<s:hidden name="actionName" value="%{actionName}" />
						<input type="submit" name="submitted"
							   value="<s:text name="payments.quittances.quittance_pay.pay"/>" class="btn-exit"
							   style="width: 100%;" />
					</td>
				</tr>
		</table>
		</form>
	</s:iterator>
</s:if>
