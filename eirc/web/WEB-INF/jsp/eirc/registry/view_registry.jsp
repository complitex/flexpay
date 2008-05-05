<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:form method="post">
	<%@include file="/WEB-INF/jsp/ab/filters/import_error_type_filter.jsp" %>
	<%@include file="../filters/registry_record_status_filter.jsp" %>
	&nbsp;&nbsp;
	<input type="submit" value="<s:text name="eirc.filter" />"/>

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr class="cols_1">
			<td class="col"><b><s:text name="eirc.registry.number"/>:</b> <s:property value="%{registry.registryNumber}"/></td>
			<td class="col"><b><s:text name="eirc.sender"/>:</b> <s:property value="%{registry.sender.name}"/></td>
		</tr>
		<tr class="cols_1">
			<td class="col"><b><s:text name="eirc.registry_type"/>:</b> <s:text name="%{registry.registryType.i18nName}"/></td>
			<td class="col"><b><s:text name="eirc.recipient"/>:</b> <s:property value="%{registry.recipient.name}"/></td>
		</tr>
		<tr class="cols_1">
			<td class="col"><b><s:text name="ab.from"/>:</b> <s:property value="%{registry.fromDate}"/>
				<b><s:text name="ab.till"/>:</b> <s:property value="%{registry.tillDate}"/></td>
			<td class="col"><b><s:text name="eirc.records_number"/>:</b> <s:property value="%{registry.recordsNumber}"/></td>
		</tr>
		<tr class="cols_1">
			<td class="col"><b><s:text name="eirc.registry.errors_number"/>:</b> <s:property value="%{registry.errorsNumber}"/></td>
			<td class="col"><b><s:text name="eirc.status"/>:</b> <s:text name="%{registry.registryStatus.i18nName}"/></td>
		</tr>
	</table>

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th">&nbsp;</td>
			<td class="th">&nbsp;</td>
			<td class="th"><s:text name="eirc.registry.record.service"/></td>
			<td class="th"><s:text name="eirc.registry.record.account"/></td>
			<td class="th"><s:text name="eirc.registry.record.address"/></td>
			<td class="th"><s:text name="eirc.registry.record.fio"/></td>
			<td class="th"><s:text name="eirc.date"/></td>
			<td class="th"><s:text name="eirc.registry.record.amount"/></td>
			<td class="th"><s:text name="eirc.registry.record.containers"/></td>
			<td class="th"><s:text name="eirc.registry.record.error"/></td>
			<td class="th"><s:text name="eirc.status"/></td>
		</tr>
		<s:iterator value="records" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s"><s:checkbox name="objectIds"/></td>
				<td class="col_1s" align="right">
					<s:property	value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
				</td>
				<td class="col"><s:property value="%{getServiceTypeName(serviceType)}"/></td>
				<td class="col"><s:property value="%{personalAccountExt}"/></td>
				<td class="col"><s:property value="%{streetType}"/> <s:property value="%{streetName}"/><br /><s:property value="%{buildingNum}"/> <s:property value="%{buildingBulkNum}"/> <s:property value="%{apartmentNum}"/></td>
				<td class="col"><s:property value="%{firstName}"/> <s:property value="%{middleName}"/> <s:property value="%{lastName}"/></td>
				<td class="col"><s:property value="%{operationDate}"/></td>
				<td class="col"><s:property value="%{amount}"/></td>
				<td class="col"><s:property value="%{containers}"/></td>
				<td class="col"><s:text name="%{importError.errorId}"/></td>
				<td class="col"><s:text name="%{recordStatus.i18nName}"/></td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="11">
				<input type="submit" value="<s:text name="eirc.process_selected" />"
					   onclick="alert('Not implemented yet'); return false;"/>
				<%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
			</td>
		</tr>
	</table>
	<s:hidden name="registry.id" />
</s:form>
