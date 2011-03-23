<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="common.language" /></td>
        <td class="th"><s:text name="payments.service_type.name" /></td>
        <td class="th"><s:text name="payments.service_type.description" /></td>
    </tr>
    <s:iterator value="serviceType.typeNames" status="rowstatus">
        <tr valign="middle" class="cols_1">
            <td class="col_1s">
                <s:property value="#rowstatus.index + 1" />
            </td>
            <td class="col">
                <s:property value="getLangName(lang)" />
                <s:if test="lang.default">
                    (default)
                </s:if>
            </td>
            <td class="col">
                <s:property value="name" />
            </td>
            <td class="col">
                <s:property value="description" />
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="4" height="3" bgcolor="#4a4f4f"></td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s" colspan="2"><s:text name="payments.service_type.code" />:</td>
        <td class="col" colspan="2">
            <s:property value="serviceType.code" />
        </td>
    </tr>
    <tr>
        <td colspan="4" height="3" bgcolor="#4a4f4f"></td>
    </tr>
    <tr>
        <td colspan="4">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="serviceTypeEdit"><s:param name="serviceType.id" value="serviceType.id" /></s:url>';"
                   value="<s:text name="common.edit" />" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="serviceTypeCorrectionsList"><s:param name="serviceType.id" value="serviceType.id" /></s:url>';"
                   value="<s:text name="payments.service_type.view_corrections" />" />
        </td>
    </tr>
</table>
