<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<div id="response">
    <%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
</div>

<script type="text/javascript">
    FP.switchSorter("regionSorterButton");
</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="4">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="regionEdit"><s:param name="region.id" value="0" /></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="<s:if test="regionSorter.activated">th_s</s:if><s:else>th</s:else>" width="63%">
            <%@include file="/WEB-INF/jsp/ab/sorters/region_sorter_header.jsp"%>
        </td>
        <td class="th" width="35%">&nbsp;</td>
    </tr>
    <s:iterator value="regions" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col_1s" align="right"><s:property value="#status.index + pager.thisPageFirstElementNumber + 1" /></td>
            <td class="col">
                <input type="checkbox" value="<s:property value="id" />" name="objectIds" />
            </td>
            <td class="col">
                <a href="<s:url action="townsList"><s:param name="regionFilter" value="id" /></s:url>"><s:property value="getTranslationName(getCurrentName().translations)" /></a>
            </td>
            <td class="col">
                <a href="<s:url action="regionView"><s:param name="object.id" value="id" /></s:url>"><s:text name="common.view" /></a>
                <a href="<s:url action="regionEdit"><s:param name="region.id" value="id" /></s:url>"><s:text name="common.edit" /></a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="4">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="regionEdit"><s:param name="region.id" value="0" /></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>
