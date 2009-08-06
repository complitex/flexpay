<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <%@include file="../filters/groups/country_region_town_street_building_ajax.jsp" %>
        </td>
    </tr>
    <tr>
        <td id="result">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="apartmentEdit"><s:param name="apartment.id" value="0"/></s:url>'"
                   value="<s:text name="common.new"/>" />
        </td>
    </tr>
</table>

<script type="text/javascript">

    var shadowId = "shadow";
    var resultId = "result";

    $(function() {

        FP.createShadow(shadowId);

        FF.addListener("building", function(filter) {
            FP.resizeShadow(shadowId, resultId, {visibility:"visible"});
            $.post("<s:url action="apartmentsListAjax" namespace="/dicts" includeParams="none"/>",
                    {buildingId: filter.value.val()},
                    function(data) {
                        $("#" + resultId).html(data);
                        FP.hideShadow(shadowId);
                    });
        });
        FF.addEraseFunction("building", function(filter) {
            $("#" + resultId).html('<input type="button" class="btn-exit" '
                    + 'onclick="window.location=\'<s:url action="apartmentEdit"><s:param name="apartment.id" value="0"/></s:url>\'" '
                    + 'value="<s:text name="common.new"/>" />');
        });
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="apartmentsListAjax" namespace="/dicts" includeParams="none"/>",
            params:{
                buildingId: FF.filters["building"].value.val(),
                "apartmentSorter.active": $("#apartmentSorterActive").val(),
                "apartmentSorter.order": $("#apartmentSorterOrder").val()
            }
        });
    }

    function sorterAjax() {
        $.post("<s:url action="apartmentsListAjax" namespace="/dicts" includeParams="none"/>",
                {
                    buildingId: FF.filters["building"].value.val(),
                    "apartmentSorter.active": $("#apartmentSorterActive").val(),
                    "apartmentSorter.order": $("#apartmentSorterOrder").val()
                },
                function(data) {
                    $("#result").html(data);
                });
    }

</script>
