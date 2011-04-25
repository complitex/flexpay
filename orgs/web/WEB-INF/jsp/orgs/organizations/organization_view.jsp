<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="common.language" /></td>
        <td class="th"><s:text name="orgs.organization.name" /></td>
    </tr>
    <s:iterator value="organization.names" status="rowstatus">
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
        <td colspan="3" height="3" bgcolor="#4a4f4f"/>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="common.language" /></td>
        <td class="th"><s:text name="orgs.organization.comment" /></td>
    </tr>
    <s:iterator value="organization.descriptions" status="rowstatus">
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
        <td colspan="3" height="3" bgcolor="#4a4f4f"/>
    </tr>
    <tr class="cols_1">
        <td class="col_1s"><s:text name="orgs.organization.kpp" />:</td>
        <td class="col" colspan="2">
            <s:property value="organization.kpp"/>
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s"><s:text name="orgs.organization.inn" />:</td>
        <td class="col" colspan="2">
            <s:property value="organization.individualTaxNumber"/>
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s"><s:text name="orgs.organization.juridical_address" />:</td>
        <td class="col" colspan="2">
            <s:property value="organization.juridicalAddress"/>
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s"><s:text name="orgs.organization.postal_address" />:</td>
        <td class="col" colspan="2">
            <s:property value="organization.postalAddress"/>
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s"><s:text name="orgs.subdivisions" />:</td>
        <td class="col" colspan="2">
            <a href="<s:url action="subdivisionsList"><s:param name="organization.id" value="organization.id" /></s:url>"><s:text name="orgs.subdivisions" /></a>
        </td>
    </tr>
    <tr>
        <td colspan="3" height="3" bgcolor="#4a4f4f"/>
    </tr>
    <tr>
        <td colspan="3">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="organizationEdit"><s:param name="organization.id" value="%{organization.id}" /></s:url>';"
                   value="<s:text name="common.edit" />" />
        </td>
    </tr>
</table>
