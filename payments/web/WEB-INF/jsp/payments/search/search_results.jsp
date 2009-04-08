<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr>
		<td class="th" width="1%">&nbsp;</td>
		<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.service"/></td>
		<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.service_supplier"/></td>
		<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.payable"/></td>
		<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.payed"/></td>
		<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.pay"/></td>
	</tr>


	<tr class="cols_1">
		<td class="col" align="right">1</td>
		<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.service1.name"/></td>
		<td class="col"><s:text name="payments.demo.data.service1.provider"/></td>
		<td class="col"><s:text name="payments.demo.data.service1.outgoing"/></td>
		<td class="col"><s:text name="payments.demo.data.service1.payed_summ"/></td>
		<td class="col"><input type="text" value="<s:text name="payments.demo.data.service1.payable"/>" style="width: 100%; text-align: right;"/></td>
	</tr>

	<tr class="cols_1">
		<td class="col" align="right">2</td>
		<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.service2.name"/></td>
		<td class="col"><s:text name="payments.demo.data.service2.provider"/></td>
		<td class="col"><s:text name="payments.demo.data.service2.outgoing"/></td>
		<td class="col"><s:text name="payments.demo.data.service2.payed_summ"/></td>
		<td class="col"><input type="text" value="<s:text name="payments.demo.data.service2.payable"/>" style="width: 100%; text-align: right;"/></td>
	</tr>


	<tr class="cols_1">
		<td class="col" colspan="3" style="text-align:right;font-weight:bold;"><s:text name="payments.quittances.quittance_pay.total_payable"/></td>
		<td class="col" style="font-weight:bold;"><s:text name="payments.demo.data.total_payable"/></td>
		<td class="col" style="font-weight:bold;"><s:text name="payments.demo.data.total_payed_before"/></td>
		<td class="col"><input type="text" name="totalPayed" value="<s:text name="payments.demo.data.total_pay"/>" style="width: 100%; text-align: right;" readonly="readonly"/></td>
	</tr>

	<tr>
		<td colspan="5" style="text-align:left;">
			<input type="button" value="<s:text name="payments.quittance.payment.pay_by_ratio"/>" class="btn-exit"
				   onclick=""/>
			<input type="button" value="<s:text name="payments.quittance.payment.pay_asc"/>" class="btn-exit" onclick=""/>
		</td>
		<td style="text-align:right;">
			<input type="submit" name="submitted" value="<s:text name="payments.quittances.quittance_pay.pay"/>"
				   class="btn-exit" style="width: 100%;"/>
		</td>
	</tr>

</table>
