<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">
    $(function() {
        $('input[name="pager.pageNumber"]').each(function() {
            this.setAttribute("onclick", "pagerAjax(this);")
        });
        $('select[name="pager.pageSize"]').each(function() {
            this.setAttribute("onchange", "pagerAjax(this);")
        });
        $('input[id="apartmentSorterButton"]').each(function() {
            this.setAttribute("onclick", this.getAttribute("onclick") + "sorterAjax();")
        });
    });
</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="4">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
            <input type="submit" class="btn-exit"
                   onclick="$('#fobjects').attr('action','<s:url action="apartmentDelete" includeParams="none" />');"
                   value="<s:text name="common.delete_selected"/>"/>
            <input type="button" class="btn-exit"
                   onclick="window.location = '<s:url action="apartmentEdit"><s:param name="apartment.id" value="0"/></s:url>'"
                   value="<s:text name="common.new"/>"/>
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
        </td>
        <td class="th" width="58%">
            <%@ include file="../../sorters/apartmentSorterHeader.jsp" %>
        </td>
        <td class="th" width="40%">&nbsp;</td>
    </tr>
<s:iterator value="%{apartments}" status="status">
    <tr valign="middle" class="cols_1">
        <td class="col_1s" align="right">
            <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
        </td>
        <td class="col">
            <input type="checkbox" value="<s:property value="%{id}"/>" name="objectIds"/>
        </td>
        <td class="col">
            <a href="<s:url action="apartmentRegistration"><s:param name="apartment.id" value="%{id}"/><s:param name="buildings.id" value="%{buildingsFilter.selectedId}"/></s:url>">
                <s:property value="%{number}"/>
            </a>
        </td>
        <td class="col">
            <a href="<s:url action="apartmentEdit"><s:param name="apartment.id" value="%{id}"/><s:param name="buildingsFilter.selectedId" value="%{buildingsFilter.selectedId}"/></s:url>">
                <s:text name="common.edit"/>
            </a>
        </td>
    </tr>
</s:iterator>
    <tr>
        <td colspan="4">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
            <input type="submit" class="btn-exit"
                   onclick="$('#fobjects').attr('action','<s:url action="apartmentDelete" includeParams="none" />');"
                   value="<s:text name="common.delete_selected"/>"/>
            <input type="button" class="btn-exit"
                   onclick="window.location = '<s:url action="apartmentEdit"><s:param name="apartment.id" value="0"/></s:url>'"
                   value="<s:text name="common.new"/>"/>
        </td>
    </tr>
</table>
