<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td id="filters" nowrap>
            <%@include file="/WEB-INF/jsp/orgs/filters/sender_organization_filter.jsp"%>
            &nbsp;&nbsp;
            <%@include file="/WEB-INF/jsp/orgs/filters/recipient_organization_filter.jsp"%>
            &nbsp;&nbsp;
            <%@include file="/WEB-INF/jsp/payments/filters/registry_type_filter.jsp"%>
            &nbsp;&nbsp;
            <%@include file="/WEB-INF/jsp/payments/filters/service_provider_filter.jsp"%>
            <br />
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

    var $registryTypeFilter = $("select[name=registryTypeFilter.selectedId]");
    var regType = <s:property value="getTypeByCode(@org.flexpay.common.persistence.registry.RegistryType@TYPE_CASH_PAYMENTS).id" />;

    $("#filters").ready(function() {
        $registryTypeFilter.get(0).onchange = function() {registryType();};
        registryType();
    });

    function registryType() {
        FP.endis("select[name=serviceProviderFilter.selectedId]", $registryTypeFilter.val() == regType);
    }

    $(function() {
        pagerAjax();
    });

    function pagerAjax(element) {

        var params = {
            "senderOrganizationFilter.selectedId":$("select[name=senderOrganizationFilter.selectedId]").val(),
            "recipientOrganizationFilter.selectedId":$("select[name=recipientOrganizationFilter.selectedId]").val(),
            "registryTypeFilter.selectedId":$registryTypeFilter.val(),
            "registrySorterByCreationDate.active": $("#registrySorterByCreationDateActive").val(),
            "registrySorterByCreationDate.order": $("#registrySorterByCreationDateOrder").val(),
            fromDate:$("input[name=fromDate]").val(),
            tillDate:$("input[name=tillDate]").val()
        };

        if ($registryTypeFilter.val() == regType) {
            params["serviceProviderFilter.selectedId"] = $("select[name=serviceProviderFilter.selectedId]").val();
        }

        FP.pagerAjax(element, {
            action:"<s:url action="registriesListAjax" namespace="/payments" includeParams="none" />", 
            params: params
        });
    }

    function sorterAjax() {
        pagerAjax();
    }

</script>
