<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:form method="post">
	<%@include file="/WEB-INF/jsp/ab/filters/import_error_type_filter.jsp" %>
	<%@include file="../filters/registry_record_status_filter.jsp" %>
	<input type="submit" value="<s:text name="eirc.filter" />" class="btn-exit"/>

	<%@include file="../data/registry_info.jsp" %>

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th">&nbsp;</td>
			<td class="th">&nbsp;</td>
			<td class="th"><s:text name="eirc.registry.record.service" /></td>
			<td class="th"><s:text name="eirc.registry.record.account" /></td>
			<td class="th"><s:text name="eirc.registry.record.address" /></td>
			<td class="th"><s:text name="eirc.registry.record.fio" /></td>
			<td class="th"><s:text name="eirc.date" /></td>
			<td class="th"><s:text name="eirc.registry.record.amount" /></td>
			<td class="th"><s:text name="eirc.registry.record.containers" /></td>
			<td class="th"><s:text name="eirc.registry.record.error" /></td>
			<td class="th"><s:text name="eirc.status" /></td>
			<td class="th"><s:text name="eirc.correspondence" /></td>
		</tr>
		<s:iterator value="records" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s"><s:checkbox name="objectIds" /></td>
				<td class="col_1s" align="right">
					<s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}" />
				</td>
				<td class="col"><s:property value="%{getServiceTypeName(serviceType)}" /></td>
				<td class="col"><s:property value="%{personalAccountExt}" /></td>
				<td class="col"><s:property value="%{streetType}" /> <s:property
						value="%{streetName}" /><br /><s:property
						value="%{buildingNum}" /> <s:property value="%{buildingBulkNum}" /> <s:property
						value="%{apartmentNum}" /></td>
				<td class="col"><s:property value="%{firstName}" /> <s:property value="%{middleName}" /> <s:property
						value="%{lastName}" /></td>
				<td class="col"><s:property value="%{operationDate}" /></td>
				<td class="col"><s:property value="%{amount}" /></td>
				<td class="col"><s:property value="%{containers}" /></td>
				<td class="col"><s:text name="%{importError.errorId}" /></td>
				<td class="col"><s:text name="%{recordStatus.i18nName}" /></td>
				<td class="col"><a href="javascript: correspondenceScreen(<s:property value="%{id}" />)"><img
						src="<s:url value="/resources/common/img/i_edit.gif" />" alt="<s:text name="common.set" />"
						title="<s:text name="common.set" />" /></a></td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="13" class="cols_1">
				<%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %> <br />
				<input type="submit" value="<s:text name="eirc.process_selected" />" class="btn-exit"
					   onclick="alert('Not implemented yet'); return false;" />
			</td>
		</tr>
	</table>
	<s:hidden name="registry.id" />
</s:form>

<script type="text/javascript">
	function correspondenceScreen(recordId) {
		var win = new Window(
			{className: "spread", title: "Corrections", top:70, left:100, width:800, height:600,
				url: '<s:url action="select_correction_type" includeParams="none"/>' + "?record.id=" + recordId})
		win.show();
	}
</script>
