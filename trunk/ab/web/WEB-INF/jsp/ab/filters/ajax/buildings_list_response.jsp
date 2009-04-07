<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="0" cellspacing="0">
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
</table>