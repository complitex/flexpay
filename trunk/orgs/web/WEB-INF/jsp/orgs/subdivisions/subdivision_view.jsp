<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="common.language"/></td>
        <td class="th"><s:text name="orgs.subdivision.name"/></td>
    </tr>
    <s:iterator value="subdivision.names" status="rowstatus">
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
        <td class="th"><s:text name="common.language"/></td>
        <td class="th"><s:text name="orgs.subdivision.description"/></td>
    </tr>
    <s:iterator value="subdivision.descriptions" status="rowstatus">
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
        <td class="col_1s"><s:text name="orgs.subdivision.real_address" />:</td>
        <td class="col" colspan="2">
            <s:property value="subdivision.realAddress"/>
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s"><s:text name="orgs.subdivision.head_organization" />:</td>
        <td class="col" colspan="2">
            <s:if test="subdivision.headOrganization != null">
                <s:property value="getTranslationName(subdivision.headOrganization.names)"/>
            </s:if><s:else>
                ----
            </s:else>
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s"><s:text name="orgs.subdivision.juridical_person" />:</td>
        <td class="col" colspan="2">
            <s:if test="subdivision.juridicalPerson != null">
                <s:property value="getTranslationName(subdivision.juridicalPerson.names)"/>
            </s:if><s:else>
                ----
            </s:else>
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s"><s:text name="orgs.subdivision.parent_subdivision" />:</td>
        <td class="col" colspan="2">
            <s:if test="subdivision.parentSubdivision != null">
                <s:property value="getTranslationName(subdivision.parentSubdivision.names)"/>
            </s:if><s:else>
                ----
            </s:else>
        </td>
    </tr>
    <tr>
        <td colspan="3" height="3" bgcolor="#4a4f4f"/>
    </tr>
    <tr>
        <td colspan="3">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="subdivisionEdit"><s:param name="organization.id" value="%{subdivision.headOrganization.id}" /><s:param name="subdivision.id" value="%{subdivision.id}" /></s:url>';"
                   value="<s:text name="common.edit" />" />
        </td>
    </tr>
</table>
