<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">
    $(function() {
		$('input[id="townSorterByNameButton"]').each(function() {
			this.setAttribute("onclick", this.getAttribute("onclick") + "sorterAjax();");
		});
		$('input[id="townSorterByTypeButton"]').each(function() {
			this.setAttribute("onclick", this.getAttribute("onclick") + "sorterAjax();");
		});
    });
</script>

<s:actionerror />

<s:form id="fobjects" action="townDelete" namespace="/dicts">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td colspan="5">
                <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp" %>
                <input type="submit" class="btn-exit" value="<s:text name="common.delete_selected"/>" />
                <input type="button" class="btn-exit"
                       onclick="window.location='<s:url action="townEdit" includeParams="none"><s:param name="town.id" value="0" /></s:url>';"
                       value="<s:text name="common.new"/>" />
            </td>
        </tr>
        <tr>
            <td class="th" width="1%">&nbsp;</td>
            <td class="th" width="1%">
                <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
            </td>
            <td class="<s:if test="townSorterByName.activated">th_s</s:if><s:else>th</s:else>" width="31%" nowrap="nowrap">
                <%@ include file="../sorters/town_sort_by_name_header.jsp" %>
            </td>
            <td class="<s:if test="townSorterByType.activated">th_s</s:if><s:else>th</s:else>" width="32%" nowrap="nowrap">
                <%@ include file="../sorters/town_sort_by_type_header.jsp" %>
            </td>
            <td class="th" width="35%">&nbsp;</td>
        </tr>
        <s:iterator value="%{towns}" status="status">
            <tr valign="middle" class="cols_1">
                <td class="col_1s" align="right">
                    <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}" />&nbsp;
                </td>
                <td class="col">
                    <input type="checkbox" value="<s:property value="%{id}"/>" name="objectIds"/>
                </td>
                <td class="col">
                    <a href="<s:url action="streetsList"><s:param name="townFilter" value="%{id}" /></s:url>"><s:property value="%{getTranslation(getCurrentName().translations).name}"/></a>
                </td>
                <td class="col">
                    <s:property value="%{getTranslation(getCurrentType().translations).name}"/>
                </td>
                <td class="col">
                    <a href="<s:url action="townView"><s:param name="object.id" value="%{id}" /></s:url>"><s:text name="common.view"/></a>&nbsp;
                    <a href="<s:url action="townEdit"><s:param name="town.id" value="%{id}" /></s:url>"><s:text name="common.edit"/></a>
                </td>
            </tr>
        </s:iterator>
        <tr>
            <td colspan="5">
                <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp" %>
                <input type="submit" class="btn-exit" value="<s:text name="common.delete_selected"/>" />
                <input type="button" class="btn-exit"
                       onclick="window.location='<s:url action="townEdit" includeParams="none"><s:param name="town.id" value="0" /></s:url>';"
                       value="<s:text name="common.new"/>" />
            </td>
        </tr>
    </table>

</s:form>
