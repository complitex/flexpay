<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<s:form action="countriesList" namespace="/dicts">
    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td class="th" width="1%">&nbsp;</td>
            <td class="<s:if test="countrySorter.activated">th_s</s:if><s:else>th</s:else>" width="63%">
                <%@ include file="../sorters/country_sorter_header.jsp" %>
            </td>
            <td class="th" width="35%"><s:text name="ab.country_shortname"/></td>
        </tr>
        <s:iterator value="countries" status="rowstatus">
            <tr valign="middle" class="cols_1">
                <td class="col_1s" align="right"><s:property value="#rowstatus.index + 1" /></td>
                <td class="col">
                    <a href="<s:url action="regionsList"><s:param name="countryFilter" value="%{id}"/></s:url>">
                        <s:property value="%{getTranslation(countryNames).name}" /></a>
                </td>
                <td class="col">
                    <s:property value="%{getTranslation(countryNames).shortName}" />
                </td>
            </tr>
        </s:iterator>
        <tr>
            <td colspan="3">
                <input type="button" class="btn-exit"
                       onclick="window.location='<s:url action="countryCreate" includeParams="none" />';"
                       value="<s:text name="common.new"/>"/>
            </td>
        </tr>
    </table>
</s:form>
