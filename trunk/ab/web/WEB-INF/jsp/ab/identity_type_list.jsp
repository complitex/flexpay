<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form id="fobjects" method="post" action="<s:url value="/dicts/identity_type_list.action" includeParams="none" />">
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'idList')"></td>
			<td class="th"><s:text name="ab.identity_type"/></td>
			<td class="th" width="35%">&nbsp;</td>
		</tr>
		<s:iterator value="translationList" status="rowstatus">
			<tr valign="middle" class="cols_1">
				<td class="col_1s"><s:property value="#rowstatus.index + 1"/></td>
				<td class="col"><input type="checkbox" name="idList" value="<s:property value="%{translatable.id}"/>" /></td>
				<td class="col">
					<a href="<s:url action='identity_type_view'><s:param name="id" value="%{translatable.id}"/></s:url>">
						<s:property value="name"/>
					</a>
				</td>
				<td class="col">
					<a href="<s:url action='identity_type_edit'><s:param name="id" value="%{translatable.id}"/></s:url>">
						<s:text name="ab.edit"/>
					</a>
				</td>
			</tr>
		</s:iterator>

		<tr>
			<td colspan="4" height="3" bgcolor="#4a4f4f"/>
		<tr>

		<tr>
			<td colspan="4">
				<input type="submit" class="btn-exit"
					   onclick="$('fobjects').action='<s:url action="identity_type_delete"/>';$('fobjects').submit()"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="identity_type_create"/>'"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
	</form>
</table>
