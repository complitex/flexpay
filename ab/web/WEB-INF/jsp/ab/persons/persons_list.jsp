<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<div id="response">
    <%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
</div>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="7">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="personEdit"><s:param name="person.id" value="0" /></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="th" width="20%"><s:text name="ab.person.last_name" /></td>
        <td class="th" width="20%"><s:text name="ab.person.first_name" /></td>
        <td class="th" width="20%"><s:text name="ab.person.middle_name" /></td>
        <td class="th" width="20%"><s:text name="ab.person.birth_date" /></td>
        <td class="th" width="18%">&nbsp;</td>
    </tr>
    <s:iterator value="persons" status="status">
        <s:set name="person" value="getPerson(id)" />
        <tr valign="middle" class="cols_1">
            <td class="col_1s" align="right"><s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />&nbsp;</td>
            <td class="col">
                <input type="checkbox" name="objectIds" value="<s:property value="id" />" />
            </td>
            <td class="col"><s:property value="#person.defaultIdentity.lastName" /></td>
            <td class="col"><s:property value="#person.defaultIdentity.firstName" /></td>
            <td class="col"><s:property value="#person.defaultIdentity.middleName" /></td>
            <td class="col"><s:property value="format(#person.defaultIdentity.birthDate)" /></td>
            <td class="col">
                <a href="<s:url action="personView"><s:param name="person.id" value="id" /></s:url>"><s:text name="common.view" /></a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="7">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="personEdit"><s:param name="person.id" value="0" /></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>
