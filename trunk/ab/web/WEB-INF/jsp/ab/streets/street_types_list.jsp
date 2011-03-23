<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<div id="response">
    <%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
</div>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="4">
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="streetTypeEdit"><s:param name="streetType.id" value="0" /></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="th"><s:text name="ab.street_type1" /></td>
        <td class="th"><s:text name="ab.street_type.short_name" /></td>
        <td class="th">&nbsp;</td>
    </tr>
    <s:iterator value="streetTypes" status="rowstatus">
        <tr valign="middle" class="cols_1">
            <td class="col_1s"><s:property value="#rowstatus.index + 1" /></td>
            <td class="col">
                <input type="checkbox" name="objectIds" value="<s:property value="id" />" />
            </td>
            <td class="col">
                <a href="<s:url action="streetTypeView"><s:param name="streetType.id" value="id" /></s:url>">
                    <s:property value="getTranslationName(translations)" />
                </a>
            </td>
            <td class="col">
                <s:property value="getTranslation(translations).shortName" />
            </td>
            <td class="col">
                <a href="<s:url action="streetTypeEdit"><s:param name="streetType.id" value="id" /></s:url>">
                    <s:text name="common.edit" />
                </a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="4">
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="streetTypeEdit"><s:param name="streetType.id" value="0" /></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>
