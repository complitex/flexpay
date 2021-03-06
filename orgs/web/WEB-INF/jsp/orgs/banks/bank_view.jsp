<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="common.language" /></td>
        <td class="th"><s:text name="orgs.bank.description" /></td>
    </tr>
    <s:iterator value="bank.descriptions" status="rowstatus">
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
        <td class="col_1s"><s:text name="orgs.bank.bank_identifier_code" />:</td>
        <td class="col" colspan="2">
            <s:property value="bank.bankIdentifierCode" />
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s"><s:text name="orgs.bank.corresponding_account" />:</td>
        <td class="col" colspan="2">
            <s:property value="bank.correspondingAccount" />
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s"><s:text name="orgs.organization" />:</td>
        <td class="col" colspan="2">
            <s:property value="getTranslationName(bank.organization.names)" />
        </td>
    </tr>
    <tr>
        <td colspan="4" height="3" bgcolor="#4a4f4f"></td>
    </tr>
    <tr>
        <td colspan="4">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="bankEdit"><s:param name="bank.id" value="%{bank.id}" /></s:url>';"
                   value="<s:text name="common.edit" />" />
        </td>
    </tr>
</table>
