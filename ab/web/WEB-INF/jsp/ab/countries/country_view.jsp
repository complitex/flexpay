<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="common.language" /></td>
        <td class="th"><s:text name="ab.country" /></td>
        <td class="th"><s:text name="ab.country.short_name" /></td>
    </tr>
    <s:iterator value="country.translations" status="rowstatus">
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
</table>
