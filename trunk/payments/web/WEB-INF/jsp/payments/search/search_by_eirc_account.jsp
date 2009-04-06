
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<s:form>

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="payments.eirc_account" />:</td>
			<td><s:textfield name="accountNumber" cssStyle="width: 300px;"/></td>            
		</tr>

		<tr>
			<td colspan="2">
                <input type="submit" name="submitted" value="<s:text name="common.search" />" class="btn-exit" />
			</td>
		</tr>

	</table>

</s:form>
