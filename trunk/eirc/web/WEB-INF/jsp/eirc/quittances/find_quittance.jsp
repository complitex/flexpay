<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<s:actionerror />

<s:form action="searchQuittance" method="POST">

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="eirc.quittance.number" /></td>
			<td><s:textfield key="quittanceNumber" /></td>
		</tr>

		<tr>
			<td colspan="2" align="center">
				&nbsp;
			</td>
		</tr>

		<tr>
			<td colspan="2" align="center">
				<s:submit name="submitted" value="%{getText('common.view')}" cssClass="btn-exit" />
			</td>
		</tr>

	</table>

</s:form>
	