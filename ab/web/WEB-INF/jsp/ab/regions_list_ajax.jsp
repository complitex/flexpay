<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:form id="fobjects" action="regionsListAjax" namespace="/dicts">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td>
                <%@include file="filters/groups/country_ajax.jsp" %>
            </td>
        </tr>
        <tr>
            <td id="result">
                <input type="submit" class="btn-exit"
                       onclick="$('#fobjects').attr('action','<s:url action="regionDelete" includeParams="none" />');"
                       value="<s:text name="common.delete_selected"/>" />
                <input type="button" class="btn-exit"
                       onclick="window.location='<s:url action="regionEdit" includeParams="none"><s:param name="region.id" value="0" /></s:url>';"
                       value="<s:text name="common.new"/>" />
            </td>
        </tr>
    </table>

</s:form>

<script type="text/javascript">
    $(function() {
        FF.addListener("country", function(filter) {
            var id = "result";
            FF.loading(id);
            $.post("<s:url action="regionsListAjax" namespace="/dicts" includeParams="none"/>",
                    {countryId: filter.value.val()},
                    function(data) {
                        $("#" + id).html(data);
                    });
        });

        FF.addEraseFunction("country", function(filter) {
            $("#result").html('<input type="submit" class="btn-exit" '
                    + 'onclick="$(\'#fobjects\').attr(\'action\',\'<s:url action="regionDelete" includeParams="none" />\');" '
                    + 'value="<s:text name="common.delete_selected"/>"/>\n'
                    + '<input type="button" class="btn-exit" '
                    + 'onclick="window.location=\'<s:url action="regionEdit" includeParams="none"><s:param name="region.id" value="0" /></s:url>\';" '
                    + 'value="<s:text name="common.new"/>"/>');
        });
    });

    function pagerAjax(element) {
        var isSelect = element.name == "pager.pageSize";
        $.post("<s:url action="regionsListAjax" namespace="/dicts" includeParams="none"/>",
                {
                    countryId: FF.filters["country"].value.val(),
					"regionSorter.active": $("#regionSorterActive").val(),
					"regionSorter.order": $("#regionSorterOrder").val(),
                    pageSizeChanged: isSelect,
                    "pager.pageNumber": isSelect ? "" : element.value,
                    "pager.pageSize": isSelect ? element.value : $('select[name="pager.pageSize"]').val()
                },
                function(data) {
                    $("#result").html(data);
                });
    }

	function sorterAjax() {
		$.post("<s:url action="regionsListAjax" namespace="/dicts" includeParams="none"/>",
				{
					countryId: FF.filters["country"].value.val(),
					"regionSorter.active": $("#regionSorterActive").val(),
					"regionSorter.order": $("#regionSorterOrder").val()
				},
				function(data) {
					$("#result").html(data);
				});
	}

</script>
