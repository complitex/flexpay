<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form id="fobjects" method="post" action="<s:url value="/dicts/buildingsList.action" includeParams="none" />">

		<tr>
			<td colspan="4">
				<%@ include file="filters/groups/country_region_town_streetname.jsp" %>
			</td>
		</tr>
		
		<tr>
			<td colspan="4">
				<%@ include file="filters/pager.jsp" %>
				<input type="submit" class="btn-exit"
				       onclick="$('#fobjects').attr('action','<s:url action="buildingDelete" includeParams="none" />');"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="button" class="btn-exit"
				       onclick="window.location='<s:url action="buildingCreate" includeParams="none" />';"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>

		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%">
                <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
			</td>
			<td class="th" ><s:text name="ab.building"/></td>
			<td class="th">&nbsp;</td>
		</tr>
		<s:iterator value="%{buildingsList}" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right">
					<s:property	value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
				</td>
				<td class="col">
					<input type="checkbox" value="<s:property value="%{id}"/>" name="objectIds"/>
				</td>
				<td class="col">
					<a href="<s:url value="/dicts/apartmentsList.action?countryFilter.selectedId=%{countryFilter.selectedId}&regionFilter.selectedId=%{regionFilter.selectedId}&townFilter.selectedId=%{townFilter.selectedId}&streetNameFilter.selectedId=%{streetNameFilter.selectedId}&buildingsFilter.selectedId=%{id}"/>">
						<s:property	value="%{getBuildingNumber(buildingAttributes)}"/>
					</a>
				</td>
				<td class="col">
					<a href="<s:url value="/dicts/buildingEdit.action?buildings.id=%{id}"/>">
						<s:text name="common.edit"/> 
					</a>
				</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="4">
				<%@ include file="filters/pager.jsp" %>
				<input type="submit" class="btn-exit"
				       onclick="$('#fobjects').attr('action','<s:url action="buildingDelete" includeParams="none" />');"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="submit" class="btn-exit"
				       onclick="$('#fobjects').attr('action','<s:url action="buildingCreate" includeParams="none" />');"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
	</form>
</table>
