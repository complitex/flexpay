<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr class="cols_1">
		<td class="col"><b><s:text name="eirc.registry.number" />:</b> <s:property
				value="%{registry.registryNumber}" /></td>
		<td class="col"><b><s:text name="eirc.sender" />:</b> <s:property value="%{registry.sender.name}" /></td>
	</tr>
	<tr class="cols_1">
		<td class="col"><b><s:text name="eirc.registry_type" />:</b> <s:text
				name="%{registry.registryType.i18nName}" /></td>
		<td class="col"><b><s:text name="eirc.recipient" />:</b> <s:property value="%{registry.recipient.name}" />
		</td>
	</tr>
	<tr class="cols_1">
		<td class="col"><b><s:text name="ab.from" />:</b> <s:property value="%{registry.fromDate}" />
			<b><s:text name="ab.till" />:</b> <s:property value="%{registry.tillDate}" /></td>
		<td class="col"><b><s:text name="eirc.records_number" />:</b> <s:property
				value="%{registry.recordsNumber}" />
		</td>
	</tr>
	<tr class="cols_1">
		<td class="col"><b><s:text name="eirc.registry.errors_number" />:</b> <s:property
				value="%{registry.errorsNumber}" /></td>
		<td class="col"><b><s:text name="eirc.status" />:</b> <s:text name="%{registry.registryStatus.i18nName}" />
		</td>
	</tr>
</table>
