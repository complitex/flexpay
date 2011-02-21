<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_building_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td id="result">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="apartmentEdit" includeParams="none"><s:param name="apartment.id" value="0" /></s:url>'"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>

<script type="text/javascript">

    var resultId = "result";

    $(function() {

        FF.addListener("building", function(filter) {
            FP.pagerAjax(null, {
                action:"<s:url action="apartmentsListAjax" namespace="/dicts" includeParams="none" />",
                params:{buildingFilter: filter.value.val()}
            });
        });
        FF.addEraser("building", function() {
            $("#" + resultId).html('<input type="button" class="btn-exit" '
                    + 'onclick="window.location=\'<s:url action="apartmentEdit" includeParams="none"><s:param name="apartment.id" value="0" /></s:url>\'" '
                    + 'value="<s:text name="common.new" />" />');
        });
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="apartmentsListAjax" namespace="/dicts" includeParams="none" />",
            params:{
                buildingFilter: FF.filters["building"].value.val(),
                "apartmentSorter.active": $("#apartmentSorterActive").val(),
                "apartmentSorter.order": $("#apartmentSorterOrder").val()
            }
        });
    }

    function sorterAjax() {
        pagerAjax();
    }

    function deleteAjax() {
        FP.serviceElements("<s:url action="apartmentDelete" namespace="/dicts" includeParams="none" />", "objectIds", pagerAjax);
    }

</script>
