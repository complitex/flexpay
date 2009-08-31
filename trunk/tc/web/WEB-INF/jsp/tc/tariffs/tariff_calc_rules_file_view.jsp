<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="ab.language"/></td>
        <td class="th"><s:text name="tc.rules_file.name"/></td>
        <td class="th"><s:text name="tc.rules_file.description"/></td>
    </tr>
    <s:iterator value="rulesFile.translations" status="rowstatus">
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
</table>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th"><s:text name="tc.rules_file.file" /></td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s">
            <s:property value="rulesFile.file.originalName"/>
        </td>
    </tr>
    <tr>
        <td>
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="rulesFileEdit"><s:param name="rulesFile.id" value="rulesFile.id" /></s:url>';"
                   value="<s:text name="common.edit" />" />
        </td>
    </tr>
</table>
