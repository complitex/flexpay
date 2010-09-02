<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_building_apartment_ajax.jsp"%>
<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="filter"><s:text name="ab.person.fio" /></td>
        <td>
            <input type="text" id="personFio" name="personFio" value="" />
            <input type="button" class="btn-exit" onclick="pagerAjax();" value="<s:text name="common.search" />" />
        </td>
        <td class="filter"><s:text name="eirc.eirc_account.output" /></td>
        <td>
            <select id="output" name="output" onchange="pagerAjax();">
                <option value="0"><s:text name="eirc.eirc_account.all" /></option>
                <option value="1" selected><s:text name="eirc.eirc_account.with_differences" /></option>
            </select>
        </td>
    </tr>
    <tr>
        <td id="result" colspan="4"></td>
    </tr>
</table>

<script type="text/javascript">

    $("#building_string").ready(function() {
        FF.addListener("building", function(filter) {
            if (FF.filters["apartment"].value.val() > 0) {
                return;
            }
            if (filter.value.val() == 0) {
                return;
            }
            pagerAjax(null, {
                buildingFilter:filter.value.val(),
                apartmentFilter:0
            });
        });
        FF.addEraser("building", function() {
            $("#result").html("");
        });
    });

    $("#apartment_string").ready(function() {
        FF.removeListener("apartment");
        FF.addListener("apartment", function(filter) {
            pagerAjax(null, {
                buildingFilter:0,
                apartmentFilter: filter.value.val()
            });
        });
    });

    function pagerAjax(element, params) {

        var af = FF.filters["apartment"].value.val();
        var bf = FF.filters["building"].value.val();
        var fio = $("#personFio").val();
        if (fio == "" && af == 0 && bf == 0) {
            alert("<s:text name="eirc.error.all_filters_are_empty" />");
            return;
        }
        var opt = {
            action: "<s:url action="eircAccountsListAjax" namespace="/eirc" includeParams="none" />",
            params: {
                output: $("#output").val(),
                buildingFilter: bf,
                apartmentFilter: af,
                "personSearchFilter.searchString": fio,
                "eircAccountSorterByAccountNumber.active": $("#eircAccountSorterByAccountNumberActive").val(),
                "eircAccountSorterByAccountNumber.order": $("#eircAccountSorterByAccountNumberOrder").val(),
                "eircAccountSorterByAddress.active": $("#eircAccountSorterByAddressActive").val(),
                "eircAccountSorterByAddress.order": $("#eircAccountSorterByAddressOrder").val()
            }
        };

        for(var o in params) {
            opt.params[o] = params[o];
        }

        FP.pagerAjax(element, opt);
    }

    function sorterAjax() {
        pagerAjax();
    }

</script>
