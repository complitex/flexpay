<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr valign="top" class="cols_1">
        <td class="col" colspan="2"><s:text name="ab.street_type" /></td>
    </tr>
    <tr valign="top" class="cols_1">
        <td class="col"><s:text name="ab.street_type.name" />:</td>
        <td class="col">
            <s:iterator value="names">
                <s:set name="l" value="%{getLang(key)}" />
                <s:textfield id="stName%{key}" name="names[%{key}]" value="%{value}" />(<s:if test="#l.default">*</s:if><s:property value="getLangName(#l)" />)<br>
            </s:iterator>
        </td>
    </tr>
    <tr valign="top" class="cols_1">
        <td class="col"><s:text name="ab.street_type.short_name" />:</td>
        <td class="col">
            <s:iterator value="shortNames">
                <s:set name="l" value="%{getLang(key)}" />
                <s:textfield id="stShortName%{key}" name="shortNames[%{key}]" value="%{value}" maxlength="5" />(<s:if test="#l.default">*</s:if><s:property value="getLangName(#l)" />)<br>
            </s:iterator>
        </td>
    </tr>
    <tr valign="middle">
        <td colspan="2">
            <input type="button" onclick="createItem($('#selectedGroupIndex').val());" class="btn-exit" value="<s:text name="common.add" />" />
        </td>
    </tr>
</table>
