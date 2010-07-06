<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="6">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="20%"><s:text name="ab.person.last_name" /></td>
        <td class="th" width="20%"><s:text name="ab.person.first_name" /></td>
        <td class="th" width="20%"><s:text name="ab.person.middle_name" /></td>
        <td class="th" width="20%"><s:text name="ab.person.birth_date" /></td>
    </tr>
    <s:iterator value="persons" status="status">
        <s:set name="person" value="getPerson(id)" />
        <tr valign="middle" class="cols_1">
            <td class="col_1s" align="right"><s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />&nbsp;</td>
            <td class="col">
                <input type="radio" name="objectIds" onclick="set(<s:property value="id" />);" value="<s:property value="id" />" />
            </td>
            <td class="col"><s:property value="#person.defaultIdentity.lastName" /></td>
            <td class="col"><s:property value="#person.defaultIdentity.firstName" /></td>
            <td class="col"><s:property value="#person.defaultIdentity.middleName" /></td>
            <td class="col"><s:property value="format(#person.defaultIdentity.birthDate)" /></td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="6">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
</table>
