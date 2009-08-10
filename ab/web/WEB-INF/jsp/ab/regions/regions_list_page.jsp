<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <%@include file="../filters/groups/country_ajax.jsp" %>
        </td>
    </tr>
    <tr>
        <td id="result">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="regionEdit"><s:param name="region.id" value="0" /></s:url>';"
                   value="<s:text name="common.new"/>" />
        </td>
    </tr>
</table>

<script type="text/javascript">

    var resultId = "result";

    $(function() {

        FF.addListener("country", function(filter) {
            FP.pagerAjax(null, {
                action:"<s:url action="regionsListAjax" namespace="/dicts" includeParams="none"/>",
                params:{countryFilter: FF.filters["country"].value.val()}
            });
        });

        FF.addEraseFunction("country", function(filter) {
            $("#" + resultId).html('<input type="button" class="btn-exit" '
                    + 'onclick="window.location=\'<s:url action="regionEdit" includeParams="none"><s:param name="region.id" value="0" /></s:url>\';" '
                    + 'value="<s:text name="common.new"/>"/>');
        });
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="regionsListAjax" namespace="/dicts" includeParams="none"/>",
            params:{
                countryFilter: FF.filters["country"].value.val(),
                "regionSorter.active": $("#regionSorterActive").val(),
                "regionSorter.order": $("#regionSorterOrder").val()
            }
        });
    }

	function sorterAjax() {
        pagerAjax(null);
	}

</script>
