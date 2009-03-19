<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="eirc.registry.number" />:</strong> <s:property value="%{registry.registryNumber}" />
        </td>
		<td class="col">
            <strong><s:text name="eirc.sender" />:</strong> <s:property value="%{getTranslation(registry.sender.names).name}" />
        </td>
	</tr>
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="eirc.registry_type" />:</strong> <s:text name="%{registry.registryType.i18nName}" />
        </td>
		<td class="col">
            <strong><s:text name="eirc.recipient" />:</strong> <s:property value="%{getTranslation(registry.recipient.names).name}" />
		</td>
	</tr>
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="ab.from" />:</strong> <s:date name="registry.fromDate" format="yyyy/MM/dd" />
			<strong><s:text name="ab.till" />:</strong> <s:date name="registry.tillDate" format="yyyy/MM/dd" />
        </td>
		<td class="col">
            <strong><s:text name="eirc.records_number" />:</strong> <s:property value="%{registry.recordsNumber}" />
		</td>
	</tr>
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="eirc.registry.errors_number" />:</strong> <s:property value="%{registry.errorsNumber}" />
        </td>
		<td class="col">
            <strong><s:text name="eirc.status" />:</strong> <s:text name="%{registry.registryStatus.i18nName}" />
		</td>
	</tr>
</table>
