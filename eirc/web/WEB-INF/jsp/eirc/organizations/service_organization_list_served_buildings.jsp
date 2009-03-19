<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:actionerror />

<s:form id="fServiceOrganizations" action="serviceOrganizationRemoveServedBuildings">
	<s:hidden name="serviceOrganization.id" />

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td class="th" width="1%">&nbsp;</td>
            <td class="th">
                <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
            </td>
            <td class="th">
                <s:text name="eirc.service_organization.served_buildings" />
            </td>
        </tr>
        <s:iterator value="buildings" status="status">
            <tr valign="middle" class="cols_1">
                <td class="col" width="1%">
                    <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}" />
                </td>
                <td class="col" width="1%">
                    <input type="checkbox" name="objectIds" value="<s:property value="%{id}"/>" />
                </td>
                <td class="col">
                    <s:property value="%{addresses[#status.index]}" />
                </td>
            </tr>
        </s:iterator>
        <tr>
            <td colspan="10">
                <%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
                <input type="submit" value="<s:text name="eirc.remove_served_buildings" />" class="btn-exit" name="submitted" />
                <input type="button" class="btn-exit" onclick="location.href='<s:url action="serviceOrganizationAddServedBuilding"><s:param name="serviceOrganization.id" value="%{serviceOrganization.id}" /></s:url>'" value="<s:text name="eirc.add_served_buildings"/>" />
            </td>
        </tr>
    </table>

</s:form>
