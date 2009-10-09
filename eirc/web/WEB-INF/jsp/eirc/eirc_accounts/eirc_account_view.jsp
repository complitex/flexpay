<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr>
		<td class="th" width="1%">&nbsp;</td>
		<td class="th" width="49%"><s:text name="eirc.consumer" /></td>
		<td class="th" width="50%"><s:text name="eirc.service" /></td>
	</tr>
	<s:iterator value="eircAccount.consumers" status="status">
		<tr valign="middle" class="cols_1">
			<td class="col_1s" align="right"><s:property value="#status.index + 1" /></td>
			<td class="col">
                <s:set name="i" value="responsiblePerson.defaultIdentity" />
				<a href="<s:url action="personView" namespace="/dicts" includeParams="none"><s:param name="person.id" value="%{responsiblePerson.id}" /></s:url>">
				    <s:property value="#i.lastName + ' ' + #i.firstName + ' ' + #i.middleName" />
                </a>
			</td>
			<td class="col">
				<a href="<s:url action="serviceEdit" namespace="/eirc" includeParams="none"><s:param name="service.id" value="%{service.id}" /></s:url>">
				    <s:property value="getServiceDescription(service)" />
                </a>
			</td>
		</tr>
	</s:iterator>
</table>
