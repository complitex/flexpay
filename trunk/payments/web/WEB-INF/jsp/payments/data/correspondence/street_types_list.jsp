<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="ab.street_type1" /></td>
        <td class="th"><s:text name="ab.street_type.short_name" /></td>
    </tr>
    <s:iterator value="streetTypes" status="rowstatus">
        <tr valign="middle" class="cols_1">
            <td class="col_1s"><s:property value="#rowstatus.index + 1" /></td>
            <td class="col">
                <input type="radio" name="objectIds" onclick="set(<s:property value="id" />);" value="<s:property value="id" />" />
            </td>
            <td class="col"><s:property value="getTranslationName(translations)" /></td>
            <td class="col"><s:property value="getTranslation(translations).shortName" /></td>
        </tr>
    </s:iterator>
</table>
