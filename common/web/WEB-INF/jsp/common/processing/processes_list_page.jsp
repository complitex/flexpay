<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td nowrap="nowrap">
            <s:text name="common.processing.process.filter.start_date" />
            <%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp"%>
        </td>
        <td nowrap="nowrap">
            <s:text name="common.processing.process.filter.end_date" />
            <%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp"%>
        </td>
        <td>
            <input type="button" class="btn-exit" value="<s:text name="common.show"/>" onclick="pagerAjax(null);" />
        </td>
    </tr>
    <tr>
        <td nowrap="nowrap">
            <s:text name="common.processing.process.filter.name" />
            <%@include file="/WEB-INF/jsp/common/processing/filters/process_name_filter.jsp"%>
        </td>
        <td nowrap="nowrap">
            <s:text name="common.processing.process.filter.state" />
            <%@include file="/WEB-INF/jsp/common/processing/filters/process_state_filter.jsp"%>
        </td>
        <td>&nbsp;</td>
    </tr>
</table>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td id="result"></td>
    </tr>
</table>

<script type="text/javascript">

    $(function() {
        FP.pagerAjax(null, {
            action:"<s:url action="processesListAjax" includeParams="none"/>"
        });
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="processesListAjax" includeParams="none"/>",
            params:{
                "beginDateFilter.stringDate": $("#beginDateFilter").val(),
                "endDateFilter.stringDate": $("#endDateFilter").val(),
                "processNameFilter.selectedId": $("#processNameFilter").val(),
                "processStateFilter.selectedId": $("#processStateFilter").val(),
                "processSorterByName.active": $("#processSorterByNameActive").val(),
                "processSorterByName.order": $("#processSorterByNameOrder").val(),
                "processSorterByStartDate.active": $("#processSorterByStartDateActive").val(),
                "processSorterByStartDate.order": $("#processSorterByStartDateOrder").val(),
                "processSorterByEndDate.active": $("#processSorterByEndDateActive").val(),
                "processSorterByEndDate.order": $("#processSorterByEndDateOrder").val(),
                "processSorterByState.active": $("#processSorterByStateActive").val(),
                "processSorterByState.order": $("#processSorterByStateOrder").val()
            }
        });
    }

    function sorterAjax() {
        pagerAjax(null);
    }

    function deleteAjax() {
        FP.deleteElements("<s:url action="processDelete" includeParams="none" />", "objectIds", pagerAjax);
    }

</script>