<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <%@include file="/WEB-INF/jsp/ab/filters/import_error_type_filter.jsp"%>
            <%@include file="/WEB-INF/jsp/payments/filters/registry_record_status_filter.jsp"%>
            <input type="button" value="<s:text name="eirc.filter" />" class="btn-exit" onclick="pagerAjax();" />
            <%@include file="/WEB-INF/jsp/payments/registry/data/registry_info.jsp"%>
            <s:if test="haveCommentary">
                <table cellpadding="3" cellspacing="1" border="0" width="100%">
                    <tr class="cols_1">
                        <td class="col">
                            <a href="<s:url action="registryCommentaryView" includeParams="none"><s:param name="registry.id" value="registry.id" /></s:url>"><s:text name="%{getText('payments.registry.commentary.view')}" /></a>
                        </td>
                    </tr>
                </table>
            </s:if>
        </td>
    </tr>
    <tr>
        <td id="result">
        </td>
    </tr>
</table>

<script type="text/javascript">

    $("#result").ready(function() {
        pagerAjax();
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="registryRecordsListAjax" namespace="/eirc" includeParams="none" />",
            params: {
                "registry.id":<s:property value="registry.id" />,
                "importErrorTypeFilter.selectedType":$("select[name=importErrorTypeFilter.selectedType]").val(),
                "recordStatusFilter.selectedId":$("select[name=recordStatusFilter.selectedId]").val()
            }
        });
    }

</script>
