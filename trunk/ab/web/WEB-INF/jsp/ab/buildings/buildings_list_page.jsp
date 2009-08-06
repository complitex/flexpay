<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <%@include file="../filters/groups/country_region_town_street_ajax.jsp" %>
        </td>
    </tr>
    <tr>
        <td id="result">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="buildingCreate" includeParams="none" />'"
                   value="<s:text name="common.new"/>" />
        </td>
    </tr>
</table>

<script type="text/javascript">

    var shadowId = "shadow";
    var resultId = "result";

    $(function() {

        FP.createShadow(shadowId);

        FF.addListener("street", function(filter) {
            FP.resizeShadow(shadowId, resultId, {visibility:"visible"});
            $.post("<s:url action="buildingsListAjax" namespace="/dicts" includeParams="none"/>",
                    {streetId: filter.value.val()},
                    function(data) {
                        $("#" + resultId).html(data);
                        FP.hideShadow(shadowId);
                    });
        });
        FF.addEraseFunction("street", function(filter) {
            $("#" + resultId).html('<input type="button" class="btn-exit" '
                    + 'onclick="window.location=\'<s:url action="buildingCreate" includeParams="none" />\';" '
                    + 'value="<s:text name="common.new"/>" />');
        });
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="buildingsListAjax" namespace="/dicts" includeParams="none"/>",
            params:{
                streetId: FF.filters["street"].value.val(),
                "buildingsSorter.active": $("#buildingsSorterActive").val(),
                "buildingsSorter.order": $("#buildingsSorterOrder").val()
            }
        });
    }

    function sorterAjax() {
        $.post("<s:url action="buildingsListAjax" namespace="/dicts" includeParams="none"/>",
                {
                    streetId: FF.filters["street"].value.val(),
                    "buildingsSorter.active": $("#buildingsSorterActive").val(),
                    "buildingsSorter.order": $("#buildingsSorterOrder").val()
                },
                function(data) {
                    $("#result").html(data);
                });
    }

</script>
