<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="common.language" /></td>
        <td class="th"><s:text name="eirc.service_provider.description" /></td>
    </tr>
    <s:iterator value="provider.descriptions" status="rowstatus">
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
    <tr class="cols_1">
        <td class="col_1s"><s:text name="eirc.organization" />:</td>
        <td class="col" colspan="2">
            <s:property value="getTranslationName(provider.organization.names)"/>
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s"><s:text name="eirc.service_organization.email" />:</td>
        <td class="col" colspan="2">
            <s:property value="provider.email"/>
        </td>
    </tr>
    <tr>
        <td colspan="3" height="3" bgcolor="#4a4f4f"></td>
    </tr>
    <tr>
        <td colspan="3">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="serviceProviderEdit"><s:param name="provider.id" value="%{provider.id}" /></s:url>';"
                   value="<s:text name="common.edit" />" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="serviceProviderCorrectionsList"><s:param name="provider.id" value="%{provider.id}" /></s:url>';"
                   value="<s:text name="orgs.service_provider.view_corrections" />" />
        </td>
    </tr>
</table>
