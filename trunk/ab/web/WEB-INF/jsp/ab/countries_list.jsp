<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr>
		<td class="th">&nbsp;</td>
		<td class="th" align="center"><input type="checkbox" onclick="select_checkboxes(this.value)">
		</td>
		<td class="th"><s:text name="ab.country_name"/></td>
		<td class="th"><s:text name="ab.country_shortname"/></td>
	</tr>
	<s:iterator value="translationList" status="rowstatus">
		<tr valign="middle" class="cols_1">
			<td class="col_1s" align="right">
				<s:property value="#rowstatus.index + 1" />
			</td>
			<td class="col" align="center">
				<input type="checkbox">
			</td>
			<td class="col"> 
				<a href="<s:url action='list_regions.action'><s:param name="countryFilter.selectedId" value="%{translatable.id}"/></s:url>">
	            	<s:property value="name" />
	            </a>
			</td>
			<td class="col">
				<s:property value="shortName" />
			</td>
		</tr>
	</s:iterator>
	<tr>
		<td colspan="4" height="3" bgcolor="#4a4f4f" /></tr>
	<tr>
		<td colspan="4">
			<input type="submit" class="btn-exit"
				   onclick="window.location='<c:url value="/dicts/create_country.action"/>'"
				   value="<s:text name="common.new"/>"/>
		</td>
	</tr>
</table>

