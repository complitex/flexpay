<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">
    $(function() {
        $('input[id="streetSorterByNameButton"]').each(function() {
            this.setAttribute("onclick", this.getAttribute("onclick") + "sorterAjax();");
        });
        $('input[id="streetSorterByTypeButton"]').each(function() {
            this.setAttribute("onclick", this.getAttribute("onclick") + "sorterAjax();");
        });
    });
</script>

<s:form id="fobjects" action="streetDelete" namespace="/dicts">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td colspan="5">
                <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp" %>
                <input type="submit" class="btn-exit" value="<s:text name="common.delete_selected"/>"/>
                <input type="button" class="btn-exit"
                       onclick="window.location = '<s:url action="streetEdit" includeParams="none"><s:param name="street.id" value="0"/></s:url>'"
                       value="<s:text name="common.new"/>"/>
            </td>
        </tr>
        <tr>
            <td class="th" width="1%">&nbsp;</td>
            <td class="th" width="1%">
                <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
            </td>
            <td class="<s:if test="streetSorterByName.activated">th_s</s:if><s:else>th</s:else>" width="31%" nowrap="nowrap">
                <%@ include file="../sorters/street_sort_by_name_header.jsp" %>
            </td>
            <td class="<s:if test="streetSorterByType.activated">th_s</s:if><s:else>th</s:else>" width="32%" nowrap="nowrap">
                <%@ include file="../sorters/street_sort_by_type_header.jsp" %>
            <td class="th" width="35%">&nbsp;</td>
        </tr>
        <s:iterator value="%{streets}" status="status">
            <tr valign="middle" class="cols_1">
                <td class="col" align="right">
                    <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
                </td>
                <td class="col">
                    <input type="checkbox" value="<s:property value="%{id}"/>" name="objectIds"/>
                </td>
                <td class="col">
                    <a href="<s:url action="buildingsList"><s:param name="streetFilter" value="%{id}" /></s:url>"><s:property value="%{getTranslation(getCurrentName().translations).name}"/></a>
                </td>
                <td class="col">
                    <s:property value="%{getTranslation(getCurrentType().translations).name}"/>
                </td>
                <td class="col">
                    <a href="<s:url action="streetView"><s:param name="object.id" value="%{id}" /></s:url>"><s:text name="common.view"/></a>&nbsp;
                    <a href="<s:url action="streetEdit"><s:param name="street.id" value="%{id}" /></s:url>"><s:text name="common.edit"/></a>
                </td>
            </tr>
        </s:iterator>
        <tr>
            <td colspan="5">
                <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp" %>
                <input type="submit" class="btn-exit" value="<s:text name="common.delete_selected"/>"/>
                <input type="button" class="btn-exit"
                       onclick="window.location = '<s:url action="streetEdit" includeParams="none"><s:param name="street.id" value="0"/></s:url>'"
                       value="<s:text name="common.new"/>"/>
            </td>
        </tr>
    </table>

</s:form>
