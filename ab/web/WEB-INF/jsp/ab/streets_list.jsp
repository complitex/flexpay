<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<form id="fobjects" method="post"
	  action="<s:url value="/dicts/list_streets.action" includeParams="none" />">
	<%@ include file="filters/groups/country_region_town_streetname.jsp" %>
	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<tr>
			<td colspan="5">
				<%@ include file="filters/pager.jsp" %>
				<input type="button" class="btn-exit"
					   onclick="$('fobjects').action='<s:url action="delete_streets"/>';$('fobjects').submit()"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="create_street"/>'"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox"
											 onchange="FP.setCheckboxes(this.checked, 'objectIds')">
			</td>
			<td class="th" width="32%"><s:text name="ab.street_type"/></td>
			<td class="th" width="31%"><s:text name="ab.street"/></td>
			<td class="th" width="35%">&nbsp;</td>
		</tr>
		<s:iterator value="%{objectNames}" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right">
					<s:property
							value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
				</td>
				<td class="col">
					<input type="checkbox" value="<s:property value="%{id}"/>"
						   name="objectIds"/>
				</td>
				<td class="col">
					<s:property
							value="%{getTranslation(getCurrentType().translations).name}"/>
				</td>
				<td class="col">
					<a href="<s:url value="/dicts/list_buildings.action?countryFilter.selectedId=%{countryFilter.selectedId}&regionFilter.selectedId=%{regionFilter.selectedId}&townFilter.selectedId=%{townFilter.selectedId}&streetNameFilter.selectedId=%{id}"/>">
						<s:property
								value="%{getTranslation(getCurrentName().translations).name}"/>
					</a>
				</td>
				<td class="col">
					<a href="<s:url value="/dicts/view_street.action?object.id=%{id}"/>">
						<s:text name="common.view"/>
					</a>
				</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="5">
				<%@ include file="filters/pager.jsp" %>
				<input type="button" class="btn-exit"
					   onclick="$('fobjects').action='<s:url action="delete_streets"/>';$('fobjects').submit()"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="create_street"/>'"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
	</table>
</form>
