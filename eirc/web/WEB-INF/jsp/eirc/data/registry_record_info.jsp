<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr class="cols_1">
		<td class="col"><strong><s:text name="eirc.registry.record.service" />:</strong> <s:property value="%{getServiceTypeName(record.serviceType)}" /></td>
		<td class="col"><strong><s:text name="eirc.registry.record.account" />:</strong> <s:property value="%{record.personalAccountExt}" /></td>
	</tr>
	<tr class="cols_1">
		<td class="col"><strong><s:text name="eirc.registry.record.address" />:</strong> <s:property value="%{record.streetType}" /> <s:property
						value="%{record.streetName}" />, <s:property value="%{record.buildingNum}" /> <s:property value="%{record.buildingBulkNum}" />, <s:property value="%{record.apartmentNum}" /></td>
		<td class="col"><strong><s:text name="eirc.registry.record.fio" />:</strong>
			<s:property value="%{record.firstName}" /> <s:property value="%{record.middleName}" />
			<s:property value="%{record.lastName}" /></td>
	</tr>
	<tr class="cols_1">
		<td class="col"><strong><s:text name="eirc.date" />:</strong> <s:date format="yyyy/MM/dd" name="record.operationDate" /></td>
		<td class="col"><strong><s:text name="eirc.registry.record.amount" />:</strong> <s:property value="%{record.amount}" /></td>
	</tr>
	<tr class="cols_1">
		<%--<td class="col"><strong><s:text name="eirc.registry.record.containers" />:</strong> <s:property value="%{record.containers}" /></td>--%>
		<td class="col"><strong><s:text name="eirc.registry.record.containers" />:</strong> N/A</td>
		<td class="col"><strong><s:text name="eirc.registry.record.error" />:</strong> <s:text name="%{record.importError.errorId}" /></td>
	</tr>
</table>
