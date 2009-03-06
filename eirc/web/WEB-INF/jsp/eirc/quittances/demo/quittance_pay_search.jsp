<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<s:form action="demoQuittancePaySearch">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="eirc.quittance.number" />:</td>
			<td><s:textfield name="quittanceNumber" /></td>
		</tr>

		<tr>
			<td colspan="2">
                <s:submit name="submitted" value="%{getText('common.search')}" cssClass="btn-exit" />
			</td>
		</tr>

	</table>

</s:form>