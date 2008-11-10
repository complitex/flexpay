<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<s:form action="town_type_delete">
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%">
				<input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'idList');">
			</td>
			<td class="th"><s:text name="ab.town_type"/></td>
			<td class="th">&nbsp;</td>
		</tr>
		<s:iterator value="translationList" status="rowstatus">
			<tr valign="middle" class="cols_1">
				<td class="col_1s"><s:property value="#rowstatus.index + 1"/></td>
				<td class="col"><s:checkbox name="idList" fieldValue="%{translatable.id}"/></td>
				<td class="col">
					<a href="<s:url action='town_type_view'><s:param name="id" value="%{translatable.id}"/></s:url>">
						<s:property value="name"/>
					</a>
				</td>
				<td class="col">
					<a href="<s:url action='town_type_edit'><s:param name="id" value="%{translatable.id}"/></s:url>">
						<s:text name="ab.edit"/>
					</a>
				</td>
			</tr>
		</s:iterator>

		<tr>
			<td colspan="5">
				<s:submit name="submitted" value="%{getText('ab.delete')}"
						  cssClass="btn-exit"/>

				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action='town_type_create'/>';"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>


	</s:form>
</table>