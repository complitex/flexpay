<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<table cellpadding="3" cellspacing="1" border="0" width="100%">

	<tr>
		<td class="th" width="1%">&nbsp;</td>
		<td class="th" width="63%"><s:text name="eirc.consumer"/></td>
	</tr>
	<s:iterator value="%{eircAccount.consumers}" status="status">
		<tr valign="middle" class="cols_1">
			<td class="col_1s" align="right"><s:property value="%{#status.index + 1}"/>
				&nbsp;
			</td>
			<td class="col">
				<s:property value="%{responsiblePerson.defaultIdentity.lastName}"/>
				<s:property value="%{responsiblePerson.defaultIdentity.firstName}"/>
				<s:property value="%{responsiblePerson.defaultIdentity.middleName}"/>
			</td>
		</tr>
	</s:iterator>

</table>
