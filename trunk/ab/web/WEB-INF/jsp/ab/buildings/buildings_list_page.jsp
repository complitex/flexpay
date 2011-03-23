<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td id="result">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="buildingCreate" />'"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>

<script type="text/javascript">

    var resultId = "result";

    $(function() {

        FF.addListener("street", function(filter) {
            FP.pagerAjax(null, {
                action:"<s:url action="buildingsListAjax" namespace="/dicts" />",
                params:{streetFilter: filter.value.val()}
            });
        });

        FF.addEraser("street", function() {
            $("#" + resultId).html('<input type="button" class="btn-exit" '
                    + 'onclick="window.location=\'<s:url action="buildingCreate" />\';" '
                    + 'value="<s:text name="common.new" />" />');
        });
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="buildingsListAjax" namespace="/dicts" />",
            params:{
                streetFilter: FF.filters["street"].value.val(),
                "buildingsSorter.active": $("#buildingsSorterActive").val(),
                "buildingsSorter.order": $("#buildingsSorterOrder").val()
            }
        });
    }

    function sorterAjax() {
        pagerAjax();
    }

    function deleteAjax() {
        FP.serviceElements("<s:url action="buildingDelete" namespace="/dicts" />", "objectIds", pagerAjax);
    }

</script>
