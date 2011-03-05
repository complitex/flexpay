<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_core.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bbq.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_simplemodal.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_collapser.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <%@include file="/WEB-INF/jsp/payments/filters/registry_record_status_filter.jsp"%>
            <span id="errorTypeBlock" style="display:none;">
                <%@include file="/WEB-INF/jsp/ab/filters/import_error_type_filter.jsp"%>
            </span>

            <input type="button" value="<s:text name="eirc.filter" />" class="btn-exit" onclick="pagerAjax();" />
            <%@include file="/WEB-INF/jsp/payments/registry/data/registry_info.jsp"%>
        </td>
    </tr>
    <tr>
        <td id="result">
        </td>
    </tr>
</table>

<script type="text/javascript">

    $("select[name='recordStatusFilter.selectedId']").change(function() {
        if (this.value == <s:property value="errorStatusCode" />) {
            $("#errorTypeBlock").show("fast");
        } else {
            $("#errorTypeBlock").hide("fast");
            $("select[name='importErrorTypeFilter.selectedType']").val(-1);
        }
    });

    $("select[name='recordStatusFilter.selectedId']").change();

    $("#result").ready(function() {
        pagerAjax();
    });

    function pagerAjax(element) {

        FP.pagerAjax(element, {
            action: "<s:url action="registryRecordsListAjax" namespace="/payments" includeParams="none" />",
            params: {
                "registry.id":<s:property value="registry.id" />,
                "importErrorTypeFilter.selectedType" : $("select[name='importErrorTypeFilter.selectedType']").val(),
                "recordStatusFilter.selectedId" : $("select[name='recordStatusFilter.selectedId']").val()
            }
        });
    }

</script>
