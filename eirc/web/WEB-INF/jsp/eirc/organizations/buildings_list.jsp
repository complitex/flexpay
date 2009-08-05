<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">
    $(function() {
        FF.updatePager("pagerAjax(this);");
    });
</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="4">
            <%@ include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
            <input type="submit" class="btn-exit" name="submitted" value="<s:text name="eirc.add_served_buildings"/>"/>
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="serviceOrganizationsList" includeParams="none" />'"
                   value="<s:text name="common.cancel"/>"/>
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
        </td>
        <td class="th" >
            <s:text name="ab.building"/>
        </td>
    </tr>
    <s:iterator value="%{buildings}" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col_1s" align="right">
                <s:property	value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
            </td>
            <td class="col">
                <input type="checkbox" value="<s:property value="%{building.id}"/>" name="objectIds"/>
            </td>
            <td class="col">
                <s:property	value="%{@org.flexpay.ab.util.TranslationUtil@getBuildingNumber(buildingAttributes, userPreferences.locale)}"/>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="4">
            <%@ include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
            <input type="submit" class="btn-exit" name="submitted" value="<s:text name="eirc.add_served_buildings"/>"/>
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="serviceOrganizationsList" includeParams="none" />'"
                   value="<s:text name="common.cancel"/>"/>
        </td>
    </tr>
</table>
