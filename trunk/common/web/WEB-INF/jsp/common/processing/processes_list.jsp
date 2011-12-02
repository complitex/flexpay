<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">
    FP.switchSorter(["processSorterByNameButton", "processSorterByStartDateButton", "processSorterByEndDateButton", "processSorterByStateButton"]);
</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="9">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
            <input type="button" class="btn-exit" value="<s:text name="common.process.cleanup" />"
				   onclick="window.location='<s:url action="processesCleanup" />';" />
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="th"><s:text name="common.processing.process.id" /></td>
        <td class="<s:property value="processSorterByName.activated ? 'th_s' : 'th'" />" nowrap>
            <%@include file="sorters/process_sort_by_name_header.jsp"%>
        </td>
        <td class="<s:property value="processSorterByStartDate.activated ? 'th_s' : 'th'" />" nowrap>
            <%@include file="sorters/process_sort_by_start_date_header.jsp"%>
        </td>
        <td class="<s:property value="processSorterByEndDate.activated ? 'th_s' : 'th'" />" nowrap>
            <%@include file="sorters/process_sort_by_end_date_header.jsp"%>
        </td>
        <td class="th"><s:text name="common.processing.process.user" /></td>
        <td class="<s:property value="processSorterByState.activated ? 'th_s' : 'th'" />" nowrap>
            <%@include file="sorters/process_sort_by_state_header.jsp"%>
        </td>
        <td class="th"><s:text name="common.processing.process.pause" /></td>
    </tr>
    <s:iterator value="processes" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col" width="1%">
                <s:property value="pager.thisPageFirstElementNumber + #status.index + 1" />
            </td>
            <td class="col" width="1%">
                <input type="checkbox" name="objectIds" value="<s:property value="id" />" />
            </td>
            <td class="col">
                <s:property value="id" />
            </td>
            <td class="col">
                <a href="<s:url action="processView"><s:param name="process.id" value="%{id}" /></s:url>">
                    <s:text name="%{processDefinitionId}" />
                </a>
            </td>
            <td class="col" nowrap>
                <s:date name="startDate" format="yyyy/MM/dd HH:mm" />
            </td>
            <td class="col" nowrap>
                <s:date name="endDate" format="yyyy/MM/dd HH:mm" />
            </td>
            <td class="col">&nbsp;</td>
            <td class="col">
                <s:property value="getTranslation(state)" />
            </td>
            <td class="col">&nbsp;</td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="9">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
            <input type="button" class="btn-exit" value="<s:text name="common.process.cleanup" />"
				   onclick="window.location='<s:url action="processesCleanup" />';" />
        </td>
    </tr>
</table>
