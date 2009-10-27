<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="3">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="addressAttributeTypeEdit" includeParams="none"><s:param name="attributeType.id" value="0" /></s:url>'"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="ab.buildings.attribute_type" /></td>
        <td class="th">&nbsp;</td>
    </tr>
    <s:iterator value="types" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col_1s"><s:property value="#status.index + 1" /></td>
            <td class="col_1s">
                <a href="<s:url action="addressAttributeTypeView" includeParams="none"><s:param name="attributeType.id" value="id" /></s:url>">
                    <s:property value="getTranslationName(translations)" />
                </a>
            </td>
            <td class="col_1s">
                <a href="<s:url action="addressAttributeTypeEdit" includeParams="none"><s:param name="attributeType.id" value="id" /></s:url>">
                    <s:text name="common.edit" />
                </a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="3">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="addressAttributeTypeEdit" includeParams="none"><s:param name="attributeType.id" value="0" /></s:url>'"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>
