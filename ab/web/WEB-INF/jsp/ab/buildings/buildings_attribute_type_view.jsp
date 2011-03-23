<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="common.language" /></td>
        <td class="th"><s:text name="ab.buildings_attribute_type.name" /></td>
        <td class="th"><s:text name="ab.buildings_attribute_type.short_name" /></td>
    </tr>
    <s:iterator value="attributeType.translations" status="rowstatus">
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
                <s:property value="shortName" />
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="4" height="3" bgcolor="#4a4f4f"></td>
    </tr>
    <tr>
        <td colspan="4">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="addressAttributeTypeEdit"><s:param name="attributeType.id" value="attributeType.id" /></s:url>';"
                   value="<s:text name="common.edit" />" />
        </td>
    </tr>
</table>
