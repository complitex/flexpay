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
        <%--
        select * from eirc_eirc_accounts_tbl ea
where 1 = (
  select count(*)
  from (
    select distinct sum((ifnull(bool_value,0) + ifnull(int_value,0) + ifnull(long_value,0) + convert(ifnull(string_value,'0'),SIGNED) + ifnull(double_value,0) + ifnull(decimal_value,0))*type_id)
      from eirc_consumer_attributes_tbl a
        left outer join eirc_consumers_tbl consumer on a.consumer_id=consumer.id
        left outer join eirc_consumer_attribute_types_tbl type on a.type_id=type.id
      where ea.id = consumer.eirc_account_id and type.unique_code in ('ATTR_NUMBER_TENANTS', 'ATTR_NUMBER_REGISTERED_TENANTS', 'ATTR_TOTAL_SQUARE', 'ATTR_LIVE_SQUARE', 'ATTR_HEATING_SQUARE')
      group by consumer_id) sum
);
#where ea.id = 44197
;
        --%>
        <td>
            <select id="output" name="output">
                <option value="0"<s:if test="output == 0"> selected</s:if>> <s:text name="eirc.eirc_account.all" /></option>
                <option value="1"<s:if test="output == 1"> selected</s:if>><s:text name="eirc.eirc_account.with_differences" /></option>
            </select>
        </td>
    </tr>
    <tr>
        <td id="result" colspan="4"></td>
    </tr>
</table>

<script type="text/javascript">

    $("#town_string").ready(function() {
        FF.removeListener("town");
    });
    $("#street_string").ready(function() {
        FF.removeListener("street");
    });
    $("#building_string").ready(function() {
        FF.removeListener("building");
    });
    $("#apartment_string").ready(function() {
        FF.removeListener("apartment");
    });

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
            if (FF.filters["apartment"].value.val() > 0
                    || FF.filters["building"].value.val() > 0
                    || FF.filters["street"].value.val() > 0
                    || FF.filters["town"].value.val() > 0
                    || $("#personFio").val() != "") {

                pagerAjax(null, {
                    output: <s:property value="output" />,
                    apartmentFilter: <s:property value="apartmentFilter != null ? apartmentFilter : 0" />,
                    buildingFilter: <s:property value="buildingFilter != null ? buildingFilter : 0" />,
                    streetFilter: <s:property value="streetFilter != null ? streetFilter : 0" />,
                    townFilter: <s:property value="townFilter != null ? townFilter : 0" />,
                    regionFilter: <s:property value="regionFilter != null ? regionFilter : 0" />,
                    countryFilter: <s:property value="countryFilter != null ? countryFilter : 0" />,
                    "personSearchFilter.searchString": "<s:property value="personSearchFilter.searchString" />",
                    "eircAccountSorterByAccountNumber.active": <s:property value="eircAccountSorterByAccountNumber.active" />,
                    "eircAccountSorterByAccountNumber.order": "<s:property value="eircAccountSorterByAccountNumber.order" />",
                    "eircAccountSorterByAddress.active": <s:property value="eircAccountSorterByAddress.active" />,
                    "eircAccountSorterByAddress.order": "<s:property value="eircAccountSorterByAddress.order" />",
                    "pager.pageNumber":<s:property value="pager.pageNumber" />,
                    "pager.pageSize":<s:property value="pager.pageSize" />
                });

            }
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
            "pager.pageNumber":$("input[name=curPage]").val(),
            "pager.pageSize": $("select[name=pager.pageSize]:first").val()
        };

        for (var p in params) {
            pars[p] = params[p];
        }

        FP.post(url, pars);
    }

</script>
