<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form id="fobjects" method="post" action="<s:url value="/dicts/apartmentsList.action" includeParams="none" />">

		<tr>
			<td colspan="4">
				<%@ include file="filters/groups/country_region_town_streetname_building.jsp" %>
			</td>
		</tr>

		<tr>
			<td colspan="4">
				<%@ include file="filters/pager.jsp" %>
				<input type="button" class="btn-exit"
					   onclick="$('fobjects').action='<s:url action="apartmentDelete"/>';"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="submit" class="btn-exit"
					   onclick="$('fobjects').action='<s:url action='apartmentEdit'><s:param name="apartment.id" value="0"/></s:url>';"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox"
											 onchange="FP.setCheckboxes(this.checked, 'objectIds');">
			</td>
			<td colspan="2" class="th" width="98%"><s:text name="ab.apartment"/></td>
		</tr>
		<s:iterator value="%{apartments}" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right">
					<s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
				</td>
				<td class="col">
					<input type="checkbox" value="<s:property value="%{id}"/>" name="objectIds"/>
				</td>
				<td class="col">
					<a href="<s:url action='apartmentRegistration'><s:param name="apartment.id" value="%{id}"/><s:param name="buildings.id" value="%{buildingsFilter.selectedId}"/></s:url>">
						<s:property value="%{number}"/>
					</a>
				</td>
				<td class="col">
					<a href="<s:url action='apartmentEdit'><s:param name="apartment.id" value="%{id}"/><s:param name="buildingsFilter.selectedId" value="%{buildingsFilter.selectedId}"/></s:url>">
						<s:text name="common.edit"/>
					</a>
				</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="4">
				<%@ include file="filters/pager.jsp" %>
				<input type="button" class="btn-exit"
					   onclick="$('fobjects').action='<s:url action="apartmentDelete"/>';"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="submit" class="btn-exit"
					   onclick="$('fobjects').action='<s:url action='apartmentEdit'><s:param name="apartment.id" value="0"/></s:url>';"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
	</form>
</table>
