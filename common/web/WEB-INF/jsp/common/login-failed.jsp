<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">
	function checkSession() {
		$.getJSON("<s:url value="/resources/common/jsp/session_ping.jsp" />", {}, function(json) {
			if (json.result != 'OK') {
				window.location = '<s:url value="/" includeParams="none"/>';
				return;
			}
			setTimeout(checkSession, 61 * 1000);
		});
	}
	checkSession();
</script>

<s:i18n name="/i18n/common-messages">
	<div style="text-align:center;">
		<form method="POST" action="j_security_check">
			<table border="0" align="center">
				<tr>
					<td colspan="2">
						<div align="center" class="text" style="font-weight:bold;"><s:text name="login.failure" /></div>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<div align="center"><img src="<c:url value="/resources/common/img/logo.gif" />"
												 width="123" height="37" alt="FlexPay" border="0" hspace="25" vspace="6" /></div>
					</td>
				</tr>
				<tr>
					<td width="50%">
						<div class="text" style="font-weight:bold;"><s:text name="login.username" />:</div>
					</td>
					<td width="50%">
						<input type="text" name="j_username" class="form-input-txt" />
					</td>
				</tr>
				<tr>
					<td>
						<div class="text" style="font-weight:bold;"><s:text name="login.password" />:</div>
					</td>
					<td>
						<input type="password" name="j_password" class="form-input-txt" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input type="submit" class="btn-search" value="<s:text name="login.submit" />" name="j_security_check" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</s:i18n>
