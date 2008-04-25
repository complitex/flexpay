<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form id="fobjects" method="post" action="<s:url value="/dicts/list_buildings.action" includeParams="none" />">

		<tr>
			<td class="th" width="100%" colspan="4" align="center">
				<%@ include file="filters/country_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th" width="100%" colspan="4" align="center">
				<%@ include file="filters/region_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th" width="100%" colspan="4" align="center">
				<%@ include file="filters/town_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th" width="100%" colspan="4" align="center">
				<%@ include file="filters/street_filter.jsp" %>
			</td>
		</tr>

		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox"
											 onchange="FP.setCheckboxes(this.checked, 'objectIds')">
			</td>
			<td class="th" width="98%"><s:text name="ab.building"/></td>
		</tr>
		<s:iterator value="%{buildingsList}" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right">
					<s:property	value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
					&nbsp;
				</td>
				<td class="col">
					<input type="checkbox" value="<s:property value="%{id}"/>" name="objectIds"/>
				</td>
				<td class="col">
					<a href="<s:url value="/dicts/list_apartments.action?countryFilter.selectedId=%{countryFilter.selectedId}&regionFilter.selectedId=%{regionFilter.selectedId}&townFilter.selectedId=%{townFilter.selectedId}&streetFilter.selectedId=%{streetFilter.selectedId}&buildingsFilter.selectedId=%{id}"/>">
						<s:property	value="%{getBuildingNumber(buildingAttributes)}"/>
					</a>
				</td>
			</tr>
		</s:iterator>
		<tr class="cols_1">
			<td class="col" width="100%" colspan="3" align="center">
				<%@ include file="filters/pager.jsp" %>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<input type="submit" class="btn-exit"
				<%--onclick="$('fobjects').action='<s:url action="delete_buildings"/>';$('fobjects').submit()"--%>
					   onclick="alert('<s:text name="error.not_implemented" />')"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="button" class="btn-exit"
				<%--onclick="window.location='<s:url action="create_building"/>'"--%>
					   onclick="alert('<s:text name="error.not_implemented" />')"
					   value="<s:text name="common.new"/>"/>
				<input type="submit" class="btn-exit"
					   value="<s:text name="common.refresh"/>"/>
			</td>
		</tr>
	</form>
</table>
