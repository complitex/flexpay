<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/stylesheet.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="getIdentityCookie" method="POST">
	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="payments.get_identity_cookie.cashbox_id" /></td>
			<td><s:textfield name="cashboxId" /></td>
			<td><s:submit name="submitted" value="%{getText('payments.get_identity_cookie.get')}" /></td>
		</tr>
	</table>
</s:form>
