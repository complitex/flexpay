<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td id="result">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="townEdit"><s:param name="town.id" value="0" /></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>

<script type="text/javascript">

    var resultId = "result";

    $(function() {

        FF.addListener("region", function(filter) {
            FP.pagerAjax(null, {
                action:"<s:url action="townsListAjax" namespace="/dicts" />",
                params:{regionFilter: filter.value.val()}
            });
        });
        FF.addEraser("region", function() {
            $("#" + resultId).html('<input type="button" class="btn-exit" '
                    + 'onclick="window.location=\'<s:url action="townEdit"><s:param name="town.id" value="0" /></s:url>\';" '
                    + 'value="<s:text name="common.new" />" />');
        });
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="townsListAjax" namespace="/dicts" />",
            params:{
                regionFilter: FF.filters["region"].value.val(),
                "townSorterByName.active": $("#townSorterByNameActive").val(),
                "townSorterByName.order": $("#townSorterByNameOrder").val(),
                "townSorterByType.active": $("#townSorterByTypeActive").val(),
                "townSorterByType.order": $("#townSorterByTypeOrder").val()
            }
        });
    }

	function sorterAjax() {
        pagerAjax();
	}

    function deleteAjax() {
        FP.serviceElements("<s:url action="townDelete" namespace="/dicts" />", "objectIds", pagerAjax);
    }

</script>
