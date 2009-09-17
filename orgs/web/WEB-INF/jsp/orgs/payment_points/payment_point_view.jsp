<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="ab.language" /></td>
        <td class="th"><s:text name="eirc.payment_point.name" /></td>
    </tr>
    <s:iterator value="point.names" status="rowstatus">
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
        </tr>
    </s:iterator>
    <tr>
        <td colspan="4" height="3" bgcolor="#4a4f4f"></td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s"><s:text name="eirc.payment_collector" />:</td>
        <td class="col" colspan="2">
            <s:property value="getTranslationName(point.collector.organization.names)"/>
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col"><s:text name="ab.address" />:</td>
        <td class="col" colspan="2">
            <s:property value="point.address" />
        </td>
    </tr>
    <tr>
        <td colspan="4" height="3" bgcolor="#4a4f4f"></td>
    </tr>
    <tr>
        <td colspan="4">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="paymentPointEdit"><s:param name="point.id" value="%{point.id}" /></s:url>';"
                   value="<s:text name="common.edit" />" />
        </td>
    </tr>
</table>
