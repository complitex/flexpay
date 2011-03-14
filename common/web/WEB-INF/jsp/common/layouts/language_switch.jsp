<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<td class="topmenu_form" nowrap="1">
    <table cellpadding="0" cellspacing="0" border="0">
        <tr>
            <td><span class="text-small"><s:text name="header.language" />:&nbsp;</span></td>
            <td>
                <s:form method="get">
                    <select class="form-select" name="request_locale" onchange="this.form.submit();">
                        <s:iterator value="getAllLanguages()">
                            <option value="<s:property value="langIsoCode" />"<s:if test="userPreferences.languageCode == langIsoCode"> selected</s:if>><s:property value="name" /></option>
                        </s:iterator>
                    </select>
                </s:form>
            </td>
        </tr>
    </table>
</td>
