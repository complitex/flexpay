<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <table width="100%">
                <tr>
                    <td class="filter"><s:text name="payments.service.begin_date" /></td>
                    <td>
                        <%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp"%>
                    </td>
                    <td class="filter"><s:text name="payments.service.end_date" /></td>
                    <td>
                        <%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp"%>
                    </td>
                    <td class="filter"><s:text name="orgs.service_provider" /></td>
                    <td>
                        <%@include file="/WEB-INF/jsp/payments/filters/service_provider_filter.jsp"%>
                    </td>
                    <td>
                        <input type="button" value="<s:text name="payments.filter" />" class="btn-exit" onclick="pagerAjax();" />
                    </td>
                </tr>
            </table>
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
            action:"<s:url action="servicesListAjax" />",
            params:{
                "beginDateFilter.stringDate": $("#beginDateFilter").val(),
                "endDateFilter.stringDate": $("#endDateFilter").val(),
                "serviceProviderFilter.selectedId": $("#serviceProviderFilter").val()
            }
        });
    }

</script>
