<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<script type="text/javascript">
	function setCookies() {
		$.cookie('paymentPointId', $('#payment_point_id').val(), { expires: 100 });
		$('#values_form_div').html('<s:text name="payments.get_identity_cookie.cookies_successfully_set"/>');
		return false;
	}
</script>

<div id="values_form_div">
	<form action="<s:url action="getIdentityCookies"/>">
		<table border="0">
			<tr>
				<td nowrap="nowrap">
					<label for="payment_point_id"><s:text name="payments.get_identity_cookie.payment_point_id"/></label>
				</td>
				<td>
					<input type="text" name="payment_point_id" id="payment_point_id"/>
				</td>
				<td>
					<input type="button" onclick="setCookies();" value="<s:text name="payments.get_identity_cookie.get"/>">
				</td>
			</tr>
		</table>
	</form>
</div>