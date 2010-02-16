<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="buildingAttributeTypeEdit"><s:param name="attributeType.id" value="attributeType.id" /></s:url>';"
                   value="<s:text name="common.edit" />" />
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="common.language"/></td>
        <td class="th"><s:text name="bti.building.attribute.type.name"/></td>
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
        </tr>
    </s:iterator>
    <tr>
        <td colspan="3" height="3" bgcolor="#4a4f4f"></td>
    </tr>
</table>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th" colspan="2"><s:text name="bti.building.attribute.type.time_dependent" /></td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s" colspan="2">
            <s:if test="attributeType.temp"><s:text name="common.yes" /></s:if><s:else><s:text name="common.no" /></s:else>
        </td>
    </tr>
    <s:if test="attributeType.uniqueCode != null">
        <tr>
            <td class="th" colspan="2"><s:text name="bti.building.attribute.type.unique_code" /></td>
        </tr>
        <tr class="cols_1">
            <td class="col_1s" colspan="2">
                <s:property value="attributeType.uniqueCode" />
            </td>
        </tr>
    </s:if>
    <tr>
        <td class="th" colspan="2"><s:text name="bti.building.attribute.type.group" /></td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s" colspan="2">
            <s:property value="getTranslationName(attributeType.group.translations)"/>
        </td>
    </tr>
    <tr>
        <td class="th" colspan="2"><s:text name="bti.building.attribute.type.type" /></td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s" colspan="2">
            <s:text name="%{attributeType.i18nTitle}" />
        </td>
    </tr>
    <s:if test="!enumValues.isEmpty()">
        <tr>
            <td class="th" width="1%">&nbsp;</td>
            <td class="th"><s:text name="bti.building.attribute.type.enum.value"/></td>
        </tr>
        <s:iterator value="enumValues" status="rowstatus">
            <tr valign="middle" class="cols_1">
                <td class="col_1s">
                    <s:property value="#rowstatus.index + 1" />
                </td>
                <td class="col">
                    <s:property value="value" />
                </td>
            </tr>
        </s:iterator>
    </s:if>
    <tr>
        <td height="3" bgcolor="#4a4f4f" colspan="2"></td>
    </tr>
    <tr>
        <td>
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="buildingAttributeTypeEdit"><s:param name="attributeType.id" value="attributeType.id" /></s:url>';"
                   value="<s:text name="common.edit" />" />
        </td>
    </tr>
</table>
