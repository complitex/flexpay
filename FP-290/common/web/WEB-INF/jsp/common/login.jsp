<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:i18n name="/i18n/common-messages">
	<center>
		<form method="POST" action="j_security_check">
			<table border="0">
				<tr>
					<td colspan="2">
						<div align="center" class="text"><b><s:text name="login.please_login" /></b></div>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<div align="center"><img src="<c:url value="/resources/common/img/logo.gif" />"
												 width="123" height="37" alt="FlexPay" border="0" hspace="25"
												 vspace="6" /></div>
					</td>
				</tr>
				<tr>
					<td width="50%">
						<div class="text"><b><s:text name="login.username" />:</b></div>
					</td>
					<td width="50%">
						<input class="form-input-txt" type='text' name='j_username' />
					</td>
				</tr>
				<tr>
					<td>
						<div class="text"><b><s:text name="login.password" />:</b></div>
					</td>
					<td>
						<input class="form-input-txt" type="password" name="j_password" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input type="submit" class="btn-search" value='<s:text name="login.submit" />' />
					</td>
				</tr>
			</table>
		</form>
	</center>
</s:i18n>
