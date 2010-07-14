<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td id="filters">
            <%@include file="/WEB-INF/jsp/orgs/filters/sender_organization_filter.jsp"%>
            &nbsp;&nbsp;
            <%@include file="/WEB-INF/jsp/orgs/filters/recipient_organization_filter.jsp"%>
            &nbsp;&nbsp;
            <%@include file="/WEB-INF/jsp/payments/filters/registry_type_filter.jsp"%>
            <br />
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

    var $senderOrganizationFilter = $("select[name=senderOrganizationFilter.selectedId]");
    var $recipientOrganizationFilter = $("select[name=recipientOrganizationFilter.selectedId]");
    var $registryTypeFilter = $("select[name=registryTypeFilter.selectedId]");
    var $serviceProviderFilter = $("select[name=serviceProviderFilter.selectedId]");
    var $fromDate = $("input[name=fromDate]");
    var $tillDate = $("input[name=tillDate]");

    var regType = <s:property value="@org.flexpay.common.persistence.registry.RegistryType@TYPE_CASH_PAYMENTS" />;

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
            "senderOrganizationFilter.selectedId":$senderOrganizationFilter.val(),
            "recipientOrganizationFilter.selectedId":$recipientOrganizationFilter.val(),
            "registryTypeFilter.selectedId":$registryTypeFilter.val(),
            fromDate:$fromDate.val(),
            tillDate:$tillDate.val()
        };

        if ($registryTypeFilter.val() == regType) {
            params["serviceProviderFilter.selectedId"] = $serviceProviderFilter.val();
        }

        FP.pagerAjax(element, {
            action:"<s:url action="registriesListAjax" namespace="/payments" includeParams="none" />", 
            params: params
        });
    }

</script>
