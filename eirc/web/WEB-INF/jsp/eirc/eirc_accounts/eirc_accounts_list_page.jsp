<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_building_apartment_ajax.jsp"%>
<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="filter"><s:text name="ab.person.fio" /></td>
        <td>
            <s:textfield id="personFio" name="personFio" value="%{personSearchFilter.searchString}" />
            <input type="button" class="btn-exit" onclick="pagerAjax();" value="<s:text name="common.search" />" />
        </td>
        <td class="filter"><s:text name="eirc.eirc_account.output" /></td>
        <td>
            <s:select id="output" name="output" list="#{0:getText('eirc.eirc_account.all'),1:getText('eirc.eirc_account.with_differences')}" />
        </td>
    </tr>
    <tr>
        <td id="result" colspan="4"></td>
    </tr>
</table>

<script type="text/javascript">

    function pagerAjax(element, params) {

        var af = FF.filters["apartment"].value.val();
        var bf = FF.filters["building"].value.val();
        var sf = FF.filters["street"].value.val();
        var tf = FF.filters["town"].value.val();
        var fio = $("#personFio").val();
        if (fio == "" && af == 0 && bf == 0 && sf == 0 && tf == 0) {
            alert("<s:text name="eirc.error.all_filters_are_empty" />");
            return;
        }
        var opt = {
            action: "<s:url action="eircAccountsListAjax" namespace="/eirc" includeParams="none" />",
            params: {
                output: $("#output").val(),
                apartmentFilter: af,
                buildingFilter: bf,
                streetFilter: sf,
                townFilter: tf,
                "personSearchFilter.searchString": fio,
                "eircAccountSorterByAccountNumber.active": $("#eircAccountSorterByAccountNumberActive").val(),
                "eircAccountSorterByAccountNumber.order": $("#eircAccountSorterByAccountNumberOrder").val(),
                "eircAccountSorterByAddress.active": $("#eircAccountSorterByAddressActive").val(),
                "eircAccountSorterByAddress.order": $("#eircAccountSorterByAddressOrder").val()
            }
        };

        if (params != null && params != undefined) {
            for (var p in params) {
                opt.params[p] = params[p];
            }
        }

        if (af > 0) {
            opt.params["buildingFilter"] = 0;
            opt.params["streetFilter"] = 0;
            opt.params["townFilter"] = 0;
        }
        if (bf > 0) {
            opt.params["streetFilter"] = 0;
            opt.params["townFilter"] = 0;
        }
        if (sf > 0) {
            opt.params["townFilter"] = 0;
        }

        FP.pagerAjax(element, opt);
    }

    function sorterAjax() {
        pagerAjax();
    }

    <s:if test="eircAccount != null">

        $(function() {
            <s:if test="apartmentFilter != null || buildingFilter != null || streetFilter != null || townFilter != null">
                pagerAjax(null, {
                    <s:if test="output != null">output: <s:property value="output" />,</s:if>
                    <s:if test="apartmentFilter != null">apartmentFilter: <s:property value="apartmentFilter" />,</s:if>
                    <s:if test="buildingFilter != null">buildingFilter: <s:property value="buildingFilter" />,</s:if>
                    <s:if test="streetFilter != null">streetFilter: <s:property value="streetFilter" />,</s:if>
                    <s:if test="townFilter != null">townFilter: <s:property value="townFilter" />,</s:if>
                    <s:if test="regionFilter != null">regionFilter: <s:property value="regionFilter" />,</s:if>
                    <s:if test="countryFilter != null">countryFilter: <s:property value="countryFilter" />,</s:if>
                    "personSearchFilter.searchString": "<s:property value="personSearchFilter.searchString" />",
                    "eircAccountSorterByAccountNumber.active": "<s:property value="eircAccountSorterByAccountNumber.active" />",
                    "eircAccountSorterByAccountNumber.order": "<s:property value="eircAccountSorterByAccountNumber.order" />",
                    "eircAccountSorterByAddress.active": "<s:property value="eircAccountSorterByAddress.active" />",
                    "eircAccountSorterByAddress.order": "<s:property value="eircAccountSorterByAddress.order" />",
                    "pager.pageNumber": "<s:property value="pager.pageNumber" />",
                    "pager.pageSize": "<s:property value="pager.pageSize" />"
                });
            </s:if>
        });

    </s:if>

    function f(url, params) {
        var pars = {
            output: $("#output").val(),
            apartmentFilter: FF.filters["apartment"].value.val(),
            buildingFilter: FF.filters["building"].value.val(),
            streetFilter: FF.filters["street"].value.val(),
            townFilter: FF.filters["town"].value.val(),
            regionFilter: FF.filters["region"].value.val(),
            countryFilter: FF.filters["country"].value.val(),
            "personSearchFilter.searchString": $("#personFio").val(),
            "eircAccountSorterByAccountNumber.active": $("#eircAccountSorterByAccountNumberActive").val(),
            "eircAccountSorterByAccountNumber.order": $("#eircAccountSorterByAccountNumberOrder").val(),
            "eircAccountSorterByAddress.active": $("#eircAccountSorterByAddressActive").val(),
            "eircAccountSorterByAddress.order": $("#eircAccountSorterByAddressOrder").val(),
            "pager.pageNumber":$("input[name='curPage']").val(),
            "pager.pageSize": $("select[name='pager.pageSize']:first").val()
        };

        if (params != null && params != undefined) {
            for (var p in params) {
                pars[p] = params[p];
            }
        }

        FP.post(url, pars);
    }

</script>
