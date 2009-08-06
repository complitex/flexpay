<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="5">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp" %>
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
        </td>
        <td class="th"><s:text name="tc.building_attributes.building"/></td>
        <td class="th">&nbsp;</td>
        <td class="th">&nbsp;</td>
    </tr>
    <s:iterator value="%{buildings}" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col_1s" align="right">
                <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
            </td>
            <td class="col">
                <input type="checkbox" value="<s:property value="%{id}"/>" name="objectIds"/>
            </td>
            <td class="col">
                <a href="<s:url action="buildingAttributesEdit"><s:param name="building.id" value="%{id}"/></s:url>"><s:property value="%{@org.flexpay.ab.util.TranslationUtil@getBuildingNumber(buildingAttributes, userPreferences.locale)}"/></a>
            </td>
            <td class="col">
                <a href="<s:url action="buildingAttributesEdit"><s:param name="building.id" value="%{id}"/></s:url>"><s:text name="tc.building_attributes"/></a>
            </td>
            <td class="col">
                <a href="<s:url action="buildingTCResultsList"><s:param name="buildingId" value="%{id}"/></s:url>"><s:text name="tc.building_tc_results"/></a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="5">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp" %>
        </td>
    </tr>
</table>
