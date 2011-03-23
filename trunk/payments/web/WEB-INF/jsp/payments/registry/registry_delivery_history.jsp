<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="8">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit" value="<s:text name="payments.registry.delivery_history.repeat_send" />" onclick="sendAjax();" />
        </td>
    </tr>
    <tr>
        <td class="th">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="th"><s:text name="payments.registry.delivery_history.registry_id" /></td>
        <td class="th"><s:text name="payments.registry.delivery_history.date_from" /></td>
        <td class="th"><s:text name="payments.registry.delivery_history.date_to" /></td>
        <td class="th"><s:text name="payments.registry.delivery_history.delivery_date" /></td>
        <td class="th"><s:text name="payments.registry.delivery_history.type_registry" /></td>
        <td class="th"><s:text name="payments.registry.delivery_history.recipient" /></td>
        <td class="th"><s:text name="payments.registry.delivery_history.service_provider" /></td>
    </tr>
    <s:iterator value="deliveryHistory">
        <tr>
            <td class="col" width="1%">
                <input type="checkbox" name="objectIds" value="<s:property value="id" />" />
            </td>
            <td class="col">
                <a href="<s:url action="registryView"><s:param name="registry.id" value="registryId" /></s:url>"><s:property value="registryId" /></a>
            </td>
            <td class="col"><s:property value="dateFrom" /></td>
            <td class="col"><s:property value="dateTo" /></td>
            <td class="col"><s:property value="dateDelivery" /></td>
            <td class="col"><s:property value="typeRegistry" /></td>
            <td class="col"><s:property value="recipient" /></td>
            <td class="col"><s:property value="serviceProvider" /></td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="8">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit" value="<s:text name="payments.registry.delivery_history.repeat_send" />" onclick="sendAjax();" />
        </td>
    </tr>
</table>
