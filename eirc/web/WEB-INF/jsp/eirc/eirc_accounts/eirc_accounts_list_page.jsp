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
    </tr>
    <tr>
        <td id="result" colspan="2"></td>
    </tr>
</table>

<script type="text/javascript">

    var opt = {
        action:"<s:url action="eircAccountsListAjax" namespace="/eirc" includeParams="none" />",
        params:{}
    };

    var resultId = "result";

    $(function() {

        FF.addListener("building", function(filter) {
            if (FF.filters["apartment"].value.val() > 0) {
                return;
            }
            if (filter.value.val() == 0) {
                return;
            }
            opt["params"] = {
                buildingFilter: filter.value.val(),
                personFio: ""
            };
            FP.pagerAjax(null, opt);
        });
        FF.addEraser("building", function() {
            $("#" + resultId).html("");
        });

        FF.addListener("apartment", function(filter) {
            opt["params"] = {
                apartmentFilter: filter.value.val(),
                personFio: ""
            };
            FP.pagerAjax(null, opt);
        });

    });

    function pagerAjax(element) {
        var af = FF.filters["apartment"].value.val();
        var bf = FF.filters["building"].value.val();
        var fio = $("#personFio").val();
        if (fio == "" && af == 0 && bf == 0) {
            alert("<s:text name="eirc.error.all_filters_are_empty" />");
            return;
        }
        opt["params"] = {
            apartmentFilter: af,
            buildingFilter: bf,
            personFio: fio
        };
        FP.pagerAjax(element, opt);
    }

</script>
