<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<s:actionerror/>
<s:actionmessage/>

<s:form action="getIdentityCookie">
	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td nowrap="nowrap"><s:text name="payments.get_identity_cookie.payment_point_id"/></td>
			<td><s:textfield name="paymentPointId"/></td>
			<td><s:submit name="submitted" value="%{getText('payments.get_identity_cookie.get')}"/></td>
		</tr>
	</table>
</s:form>