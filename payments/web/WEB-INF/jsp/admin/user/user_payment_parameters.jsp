<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="userPaymentParametersEdit"  method="post">

	<s:hidden name="preference.username" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="admin.user.name" />:</td>
			<td class="col"><s:property value="preference.username" /></td>
		</tr>

    <tr valign="top" class="cols_1">
			<td class="col"><s:text name="admin.payment.parameters.payment_collector_id" />:</td>
			<td class="col"><s:textfield name="preference.paymentCollectorId" /></td>
		</tr>

		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="admin.payment.parameters.payment_point_id" />:</td>
			<td class="col"><s:textfield name="preference.paymentPointId" /></td>
		</tr>

		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="admin.payment.parameters.cashbox_id" />:</td>
			<td class="col"><s:textfield name="preference.cashboxId" /></td>
		</tr>

		<tr valign="middle" class="cols_1">
			<td colspan="2">
                <input type="submit" name="submitted" class="btn-exit" value="<s:text name="common.save" />" />
            </td>
		</tr>

	</table>

</s:form>