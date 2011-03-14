<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<div id="response">
    <%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
</div>

<script type="text/javascript">
    FP.switchSorter(["countrySorterButton"]);
</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="4">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="countryCreate" />';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="<s:if test="countrySorter.activated">th_s</s:if><s:else>th</s:else>">
            <%@include file="/WEB-INF/jsp/ab/sorters/country_sorter_header.jsp"%>
        </td>
        <td class="th"><s:text name="ab.country.short_name" /></td>
        <td class="th">&nbsp;</td>
    </tr>
    <s:iterator value="countries" status="rowstatus">
        <tr valign="middle" class="cols_1">
            <td class="col_1s" align="right"><s:property value="#rowstatus.index + 1" /></td>
            <td class="col">
                <a href="<s:url action="regionsList"><s:param name="countryFilter" value="id" /></s:url>">
                    <s:property value="getTranslationName(translations)" /></a>
            </td>
            <td class="col">
                <s:property value="getTranslation(translations).shortName" />
            </td>
            <td class="col">
                <a href="<s:url action="countryView"><s:param name="country.id" value="id" /></s:url>"><s:text name="common.view" /></a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="4">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="countryCreate" />';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>
