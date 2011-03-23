<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="10">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" value="<s:text name="eirc.remove_served_buildings" />" class="btn-exit" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="serviceOrganizationAddServedBuildingPage"><s:param name="serviceOrganization.id" value="serviceOrganization.id" /></s:url>'"
                   value="<s:text name="eirc.add_served_buildings" />" />
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="th"><s:text name="eirc.service_organization.served_buildings" /></td>
    </tr>
    <s:iterator value="buildings" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col" width="1%">
                <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
            <td class="col" width="1%">
                <input type="checkbox" name="objectIds" value="<s:property value="id" />" />
            </td>
            <td class="col">
                <s:property value="addresses[#status.index]" />
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="10">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" value="<s:text name="eirc.remove_served_buildings" />" class="btn-exit" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="serviceOrganizationAddServedBuildingPage"><s:param name="serviceOrganization.id" value="serviceOrganization.id" /></s:url>'"
                   value="<s:text name="eirc.add_served_buildings" />" />
        </td>
    </tr>
</table>
