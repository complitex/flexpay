<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <%@include file="../filters/groups/country_region_town_streetname_ajax.jsp" %>
        </td>
    </tr>
    <tr>
        <td id="result">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="streetEdit" includeParams="none"><s:param name="street.id" value="0"/></s:url>';"
                   value="<s:text name="common.new"/>"/>
        </td>
    </tr>
</table>

<script type="text/javascript">

    var shadowId = "shadow";
    var resultId = "result";

    $(function() {

        FP.createShadow(shadowId);

        FF.addListener("town", function(filter) {
            FP.resizeShadow(shadowId, resultId, {visibility:"visible"});
            $.post("<s:url action="streetsListAjax" namespace="/dicts" includeParams="none"/>",
                    {townId: filter.value.val()},
                    function(data) {
                        $("#" + resultId).html(data);
                        FP.hideShadow(shadowId);
                    });
        });
        FF.addEraseFunction("town", function(filter) {
            $("#" + resultId).html('<input type="button" class="btn-exit" '
                    + 'onclick="window.location = \'<s:url action="streetEdit"><s:param name="street.id" value="0"/></s:url>\'" '
                    + 'value="<s:text name="common.new"/>"/>');
        });
        FF.addListener("street", function(filter) {
            FP.resizeShadow(shadowId, resultId, {visibility:"visible"});
            $.post("<s:url action="streetsListAjax" namespace="/dicts" includeParams="none"/>",
                    {streetId: filter.value.val()},
                    function(data) {
                        $("#" + resultId).html(data);
                        FP.hideShadow(shadowId);
                    });
        });

    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="streetsListAjax" namespace="/dicts" includeParams="none"/>",
            params:{
                townId: FF.filters["town"].value.val(),
                "streetSorterByName.active": $("#streetSorterByNameActive").val(),
                "streetSorterByName.order": $("#streetSorterByNameOrder").val(),
                "streetSorterByType.active": $("#streetSorterByTypeActive").val(),
                "streetSorterByType.order": $("#streetSorterByTypeOrder").val()
            }
        });
    }

    function sorterAjax() {
        $.post("<s:url action="streetsListAjax" namespace="/dicts" includeParams="none"/>",
                {
                    townId: FF.filters["town"].value.val(),
                    "streetSorterByName.active": $("#streetSorterByNameActive").val(),
                    "streetSorterByName.order": $("#streetSorterByNameOrder").val(),
                    "streetSorterByType.active": $("#streetSorterByTypeActive").val(),
                    "streetSorterByType.order": $("#streetSorterByTypeOrder").val()
                },
                function(data) {
                    $("#result").html(data);
                });
    }

</script>
