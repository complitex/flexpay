<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <%@include file="/WEB-INF/jsp/ab/filters/groups/country_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td id="result">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="regionEdit" includeParams="none"><s:param name="region.id" value="0" /></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>

<script type="text/javascript">

    var resultId = "result";

    $(function() {

        FF.addListener("country", function(filter) {
            FP.pagerAjax(null, {
                action:"<s:url action="regionsListAjax" namespace="/dicts" includeParams="none" />",
                params:{countryFilter: filter.value.val()}
            });
        });

        FF.addEraser("country", function() {
            $("#" + resultId).html('<input type="button" class="btn-exit" '
                    + 'onclick="window.location=\'<s:url action="regionEdit" includeParams="none"><s:param name="region.id" value="0" /></s:url>\';" '
                    + 'value="<s:text name="common.new" />" />');
        });
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="regionsListAjax" namespace="/dicts" includeParams="none" />",
            params:{
                countryFilter: FF.filters["country"].value.val(),
                "regionSorter.active": $("#regionSorterActive").val(),
                "regionSorter.order": $("#regionSorterOrder").val()
            }
        });
    }

	function sorterAjax() {
        pagerAjax();
	}

    function deleteAjax() {
        FP.serviceElements("<s:url action="regionDelete" namespace="/dicts" includeParams="none" />", "objectIds", pagerAjax);
    }

</script>
