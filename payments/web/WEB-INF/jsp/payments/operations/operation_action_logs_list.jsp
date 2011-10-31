<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%" class="operations">
    <s:if test="operationActionLogs.isEmpty()">
        <tr>
            <td colspan="6">
                <s:text name="payments.operation_action_logs.list.not_found" />
            </td>
        </tr>
    </s:if><s:else>
        <tr>
            <td colspan="6">
                <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            </td>
        </tr>
        <tr>
            <td class="th" width="1%">&nbsp;</td>
            <td class="th" width="1%">
                <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
            </td>
            <td class="th"><s:text name="payments.operation_action_logs.list.action_date" /></td>
            <td class="th"><s:text name="payments.operation_action_logs.list.user_name" /></td>
            <td class="th"><s:text name="payments.operation_action_logs.list.action" /></td>
            <td class="th"><s:text name="payments.operation_action_logs.list.action_string" /></td>
        </tr>
        <s:iterator value="operationActionLogs" status="status">
            <tr valign="middle" class="cols_1">
                <td class="col" width="1%">
                    <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
                </td>
                <td class="col" width="1%">
                    <input type="checkbox" name="objectIds" value="<s:property value="id" />" />
                </td>
                <td><s:date name="actionDate" format="yyyy.MM.dd HH:mm:ss" /></td>
                <td><s:property value="userName" /></td>
                <td>
                    <s:if test="action == 1">
                        <s:text name="payments.operation_action_logs.list.action.search_by_address" />
                    </s:if><s:elseif test="action == 2">
                        <s:text name="payments.operation_action_logs.list.action.search_by_quittance" />
                    </s:elseif><s:elseif test="action == 3">
                        <s:text name="payments.operation_action_logs.list.action.search_by_eirc_account" />
                    </s:elseif><s:elseif test="action == 4">
                        <s:text name="payments.operation_action_logs.list.action.print_quittance" />
                    </s:elseif>
                </td>
                <td><s:property value="actionString" /></td>
            </tr>
        </s:iterator>
        <tr>
            <td colspan="6">
                <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            </td>
        </tr>
    </s:else>

</table>
