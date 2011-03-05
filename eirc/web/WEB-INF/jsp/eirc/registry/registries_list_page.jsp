<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <%@include file="/WEB-INF/jsp/orgs/filters/sender_organization_filter.jsp"%>
            &nbsp;&nbsp;
            <%@include file="/WEB-INF/jsp/orgs/filters/recipient_organization_filter.jsp"%>
            &nbsp;&nbsp;
            <%@include file="/WEB-INF/jsp/payments/filters/registry_type_filter.jsp"%>
            &nbsp;&nbsp;<br />
            <span class="text">
                <s:text name="eirc.generated" />&nbsp;
                <%@include file="/WEB-INF/jsp/payments/filters/date_interval_filter.jsp"%>
            </span>
            <input type="button" value="<s:text name="eirc.filter" />" class="btn-exit" onclick="pagerAjax();" />
        </td>
    </tr>
    <tr>
        <td id="result">
        </td>
    </tr>
</table>

<script type="text/javascript">

    $(function() {
        pagerAjax();
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="registriesListAjax" namespace="/eirc" includeParams="none" />",
            params: {
                "senderOrganizationFilter.selectedId":$("select[name='senderOrganizationFilter.selectedId']").val(),
                "recipientOrganizationFilter.selectedId":$("select[name='recipientOrganizationFilter.selectedId']").val(),
                "registryTypeFilter.selectedId":$("select[name='registryTypeFilter.selectedId']").val(),
                "registrySorterByCreationDate.active": $("#registrySorterByCreationDateActive").val(),
                "registrySorterByCreationDate.order": $("#registrySorterByCreationDateOrder").val(),
                fromDate:$("input[name='fromDate']").val(),
                tillDate:$("input[name='tillDate']").val()
            }
        });
    }

    function sorterAjax() {
        pagerAjax();
    }

</script>
