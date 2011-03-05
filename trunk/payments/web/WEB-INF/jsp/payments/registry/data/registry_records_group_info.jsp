<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="eirc.registry.record.error_group.name" />:</strong> <s:property value="groupView.name" escape="false" />
        </td>
		<td class="col">
            <strong><s:text name="eirc.registry.record.error_group.number_of_errors" />:</strong> <s:property value="groupView.group.numberOfRecords" />
        </td>
	</tr>
</table>
