<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">
    FP.switchSorter("districtSorterButton");
</script>

<s:actionerror />

<s:form action="districtDelete" namespace="/dicts">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td colspan="4">
                <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp" %>
                <input type="submit" class="btn-exit" value="<s:text name="common.delete_selected"/>"/>
                <input type="button" class="btn-exit"
                       onclick="window.location='<s:url action="districtEdit" includeParams="none"><s:param name="district.id" value="0" /></s:url>';"
                       value="<s:text name="common.new"/>"/>
            </td>
        </tr>
        <tr>
            <td class="th" width="1%">&nbsp;</td>
            <td class="th" width="1%">
                <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
            </td>
            <td class="<s:if test="districtSorter.activated">th_s</s:if><s:else>th</s:else>" width="63%">
                <%@include file="../sorters/district_sorter_header.jsp" %>
            </td>
            <td class="th" width="35%">&nbsp;</td>
        </tr>
        <s:iterator value="%{districts}" status="status">
            <tr valign="middle" class="cols_1">
                <td class="col_1s" align="right">
                    <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}" />&nbsp;
                </td>
                <td class="col">
                    <input type="checkbox" value="<s:property value="%{id}"/>" name="objectIds"/>
                </td>
                <td class="col">
                    <s:property value="%{getTranslation(getCurrentName().translations).name}" />
                </td>
                <td class="col">
                    <a href="<s:url action="districtView"><s:param name="object.id" value="%{id}" /></s:url>"><s:text name="common.view" /></a>&nbsp;
                    <a href="<s:url action="districtEdit"><s:param name="district.id" value="%{id}" /></s:url>"><s:text name="common.edit" /></a>
                </td>
            </tr>
        </s:iterator>
        <tr>
            <td colspan="4">
                <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp" %>
                <input type="submit" class="btn-exit" value="<s:text name="common.delete_selected"/>"/>
                <input type="button" class="btn-exit"
                       onclick="window.location='<s:url action="districtEdit" includeParams="none"><s:param name="district.id" value="0" /></s:url>';"
                       value="<s:text name="common.new"/>"/>
            </td>
        </tr>
    </table>

</s:form>
