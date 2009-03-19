
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<s:form action="quittancePaySearch">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="eirc.quittance.number" />:</td>
			<td><s:textfield name="quittanceNumber" cssStyle="width: 300px;"/></td>            
		</tr>

		<tr>
			<td colspan="2">
                <input type="submit" name="submitted" value="<s:text name="common.search" />" class="btn-exit" />
			</td>
		</tr>

	</table>

</s:form>
