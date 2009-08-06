<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:form id="fobjects" action="apartmentsListAjax" namespace="/dicts">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td>
                <%@include file="filters/groups/country_region_town_street_ajax.jsp" %>
            </td>
        </tr>
        <tr>
            <td id="result">
                <input type="submit" class="btn-exit"
                       onclick="$('#fobjects').attr('action','<s:url action="buildingDelete" includeParams="none" />');"
                       value="<s:text name="common.delete_selected"/>"/>
                <input type="button" class="btn-exit"
                       onclick="window.location = '<s:url action="buildingCreate" includeParams="none" />'"
                       value="<s:text name="common.new"/>"/>
            </td>
        </tr>
    </table>

</s:form>

<script type="text/javascript">
    $(function() {
        FF.addListener("street", function(filter) {
            var id = "result";
            FF.loading(id);
            $.post("<s:url action="buildingsListAjax" namespace="/dicts" includeParams="none"/>",
                    {streetId: filter.value.val()},
                    function(data) {
                        $("#" + id).html(data);
                    });
        });

        FF.addEraseFunction("street", function(filter) {
            $("#result").html('<input type="submit" class="btn-exit" '
                    + 'onclick="$(\'#fobjects\').attr(\'action\',\'<s:url action="buildingDelete" includeParams="none" />\');" '
                    + 'value="<s:text name="common.delete_selected"/>"/>\n'
                    + '<input type="button" class="btn-exit" '
                    + 'onclick="window.location=\'<s:url action="buildingCreate" includeParams="none" />\';" '
                    + 'value="<s:text name="common.new"/>"/>');
        });
    });

    function pagerAjax(element) {
        var isSelect = element.name == "pager.pageSize";
        $.post("<s:url action="buildingsListAjax" namespace="/dicts" includeParams="none"/>",
                {
                    streetId: FF.filters["street"].value.val(),
                    pageSizeChanged: isSelect,
					"buildingsSorter.active": $("#buildingsSorterActive").val(),
					"buildingsSorter.order": $("#buildingsSorterOrder").val(),
                    "pager.pageNumber": isSelect ? "" : element.value,
                    "pager.pageSize": isSelect ? element.value : $('select[name="pager.pageSize"]').val()
                },
                function(data) {
                    $("#result").html(data);
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
