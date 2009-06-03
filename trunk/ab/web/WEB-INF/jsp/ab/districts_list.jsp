<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<form id="fobjects" method="post" action="">
	<%@ include file="filters/groups/country_region_town.jsp" %>
	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td colspan="4">
				<%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
				<input type="submit" class="btn-exit"
					   onclick="$('#fobjects').attr('action', '<s:url action="districtDelete" includeParams="none" />');"
					   value="<s:text name="common.delete_selected"/>" />
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="districtEdit" includeParams="none"><s:param name="district.id" value="0" /></s:url>';"
					   value="<s:text name="common.new"/>" />
			</td>
		</tr>
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%">
				<input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
			</td>
			<td class="th" width="63%"><s:text name="ab.district" /></td>
			<td class="th" width="35%">&nbsp;</td>
		</tr>
		<s:iterator value="%{objectNames}" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right">
					<s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}" />&nbsp;
				</td>
				<td class="col">
					<input type="checkbox" value="<s:property value="%{object.id}"/>" name="objectIds" />
				</td>
				<td class="col">
					<s:property value="%{getTranslation(translations).name}" />
				</td>
				<td class="col">
					<a href="<s:url action="districtView" includeParams="none"><s:param name="object.id" value="%{object.id}" /></s:url>">
						<s:text name="common.view" />
					</a>&nbsp;
					<a href="<s:url action="districtEdit" includeParams="none"><s:param name="district.id" value="%{object.id}" /></s:url>">
						<s:text name="common.edit" />
					</a>
				</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="4">
				<%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
				<input type="submit" class="btn-exit"
					   onclick="$('#fobjects').attr('action','<s:url action="districtDelete" includeParams="none" />');"
					   value="<s:text name="common.delete_selected"/>" />
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="districtEdit" includeParams="none"><s:param name="district.id" value="0" /></s:url>';"
					   value="<s:text name="common.new"/>" />
			</td>
		</tr>
	</table>
</form>
