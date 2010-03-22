<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="9">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="subdivisionEdit" includeParams="none"><s:param name="organization.id" value="%{organization.id}" /><s:param name="subdivision.id" value="0" /></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="th"><s:text name="eirc.subdivision.name" /></td>
        <td class="th"><s:text name="eirc.subdivision.description" /></td>
        <td class="th"><s:text name="eirc.subdivision.real_address" /></td>
        <td class="th"><s:text name="eirc.subdivision.head_organization" /></td>
        <td class="th"><s:text name="eirc.subdivision.juridical_person" /></td>
        <td class="th"><s:text name="eirc.subdivision.parent_subdivision" /></td>
        <td class="th">&nbsp;</td>
    </tr>
    <s:iterator value="subdivisions" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col">
                <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}" />
            </td>
            <td class="col">
                <input type="checkbox" name="objectIds" value="<s:property value="%{id}" />" />
            </td>
            <td class="col"><s:property value="repeatLevelTimes('=')" /><s:property value="getTranslation(names).name" /></td>
            <td class="col"><s:property value="getTranslation(descriptions).name" /></td>
            <td class="col"><s:property value="realAddress" /></td>
            <td class="col"><s:property value="getOrganizationName(headOrganization)" /></td>
            <td class="col"><s:property value="getOrganizationName(juridicalPerson)" /></td>
            <td class="col"><s:property value="getSubdivisionName(parentSubdivision)" /></td>
            <td class="col">
                <a href="<s:url action="subdivisionView"><s:param name="subdivision.id" value="%{id}" /></s:url>">
                     <s:text name="common.view" />
                </a>&nbsp;
                <a href="<s:url action="subdivisionEdit"><s:param name="organization.id" value="%{organization.id}" /><s:param name="subdivision.id" value="%{id}" /></s:url>">
                     <s:text name="common.edit" />
                </a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="9">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="subdivisionEdit" includeParams="none"><s:param name="organization.id" value="%{organization.id}" /><s:param name="subdivision.id" value="0" /></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>
