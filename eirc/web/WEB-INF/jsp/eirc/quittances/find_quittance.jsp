<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="quittanceSearch">
	<s:hidden name="packet.id" value="%{packet.id}" />
	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="eirc.quittance.number" />:</td>
			<td><s:textfield key="quittanceNumber" /></td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="submit" name="submitted" value="<s:text name="common.search" />" class="btn-exit" />
			</td>
		</tr>

	</table>

</s:form>
