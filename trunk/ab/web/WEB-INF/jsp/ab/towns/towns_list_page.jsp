<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <%@include file="../filters/groups/country_region_ajax.jsp" %>
        </td>
    </tr>
    <tr>
        <td id="result">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="townEdit" includeParams="none"><s:param name="town.id" value="0" /></s:url>';"
                   value="<s:text name="common.new"/>" />
        </td>
    </tr>
</table>

<script type="text/javascript">

    var shadowId = "shadow";
    var resultId = "result";

    $(function() {

        FP.createShadow(shadowId);

        FF.addListener("region", function(filter) {
            FP.resizeShadow(shadowId, resultId, {visibility:"visible"});
            $.post("<s:url action="townsListAjax" namespace="/dicts" includeParams="none"/>",
                    {regionId: filter.value.val()},
                    function(data) {
                        $("#" + resultId).html(data);
                        FP.hideShadow(shadowId);
                    });
        });
        FF.addEraseFunction("region", function(filter) {
            $("#" + resultId).html('<input type="button" class="btn-exit" '
                    + 'onclick="window.location=\'<s:url action="townEdit" includeParams="none"><s:param name="town.id" value="0" /></s:url>\';" '
                    + 'value="<s:text name="common.new"/>"/>');
        });
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="townsListAjax" namespace="/dicts" includeParams="none"/>",
            params:{
                regionId: FF.filters["region"].value.val(),
                "townSorterByName.active": $("#townSorterByNameActive").val(),
                "townSorterByName.order": $("#townSorterByNameOrder").val(),
                "townSorterByType.active": $("#townSorterByTypeActive").val(),
                "townSorterByType.order": $("#townSorterByTypeOrder").val()
            }
        });
    }

	function sorterAjax() {
		$.post("<s:url action="townsListAjax" namespace="/dicts" includeParams="none"/>",
				{
					regionId: FF.filters["region"].value.val(),
					"townSorterByName.active": $("#townSorterByNameActive").val(),
					"townSorterByName.order": $("#townSorterByNameOrder").val(),
					"townSorterByType.active": $("#townSorterByTypeActive").val(),
					"townSorterByType.order": $("#townSorterByTypeOrder").val()
				},
				function(data) {
					$("#result").html(data);
				});
	}

</script>
