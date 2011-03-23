<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

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
        <tr valign="middle"
						<s:if test="certificate != null && certificate.isExpired()">class="cols_1_red"</s:if>
						<s:elseif test="certificate != null && certificate.isBlocked()">class="cols_1_gray"</s:elseif>
						<s:elseif test="certificate != null && !certificate.isTimeUpdate()">class="cols_1_green"</s:elseif>
						<s:elseif test="certificate != null && certificate.isTimeUpdate()">class="cols_1_yellow"</s:elseif>
						<s:else>class="cols_1"</s:else>>
            <td class="col" align="right">
                <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
            <td class="col">
                <input type="checkbox" value="<s:property value="username" />" name="objectIds" />
            </td>
            <td class="col">
                <s:property value="username" />
            </td>
            <td class="col">
                <a href="<s:url action="editUser"><s:param name="model.userName" value="username" /></s:url>"><s:text name="common.edit" /></a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="4">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
									 onclick="window.location='<s:url action="createUser"/>'"
									 value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>
