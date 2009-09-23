<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<td class="topmenu_form" nowrap="1">
    <table cellpadding="0" cellspacing="0" border="0">
        <tr>
            <td><span class="text-small"><s:text name="header.language"/>:&nbsp;</span></td>
            <s:form method="get">
            <td>
                <select class="form-select" name="request_locale" onchange="this.form.submit();">
                    <s:iterator value="@org.flexpay.common.util.config.ApplicationConfig@getLanguages()" >
                        <option value="<s:property value="%{langIsoCode}" />"<s:if test="%{userPreferences.languageCode == langIsoCode}"> selected</s:if>><s:property value="name" /></option>
                    </s:iterator>
                </select>
            </td>
            </s:form>
        </tr>
    </table>
</td>
