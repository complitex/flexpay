<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr class="cols_1">
        <td class="col_1s"><s:text name="payments.registry.commentary.payment_date" />:</td>
        <td class="col">
            <s:property value="paymentDate" />
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s"><s:text name="payments.registry.commentary.payment_number" />:</td>
        <td class="col">
            <s:property value="paymentNumber" />
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s"><s:text name="payments.registry.commentary" />:</td>
        <td class="col">
            <s:property value="commentary" />
        </td>
    </tr>
    <tr>
        <td colspan="2" height="3" bgcolor="#4a4f4f"></td>
    </tr>
    <tr>
        <td colspan="2">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="registryCommentaryEdit" includeParams="none"><s:param name="registry.id" value="registry.id" /></s:url>';"
                   value="<s:text name="common.edit" />" />
        </td>
    </tr>
</table>
