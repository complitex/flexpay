<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<div id="response">
    <%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
</div>

<script type="text/javascript">
    FP.switchSorter("userSorterButton");
</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="th" width="58%"><s:text name="admin.user.name" /></td>
        <td class="th" width="40%">&nbsp;</td>
    </tr>
    <s:iterator value="allUserPreferences" status="status" id="currentUserPreferences">
        <tr valign="middle" class="cols_1">
            <td class="col_1s" align="right">
                <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
            <td class="col">
                <input type="checkbox" value="<s:property value="username" />" name="objectIds" />
            </td>
            <td class="col">
                <s:property value="username" />
            </td>
            <td class="col">
                <a href="<s:url action="editUser" includeParams="none"><s:param name="userName" value="username" /></s:url>"><s:text name="common.edit"/></a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="4">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
            <input type="button" class="btn-exit" value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>
