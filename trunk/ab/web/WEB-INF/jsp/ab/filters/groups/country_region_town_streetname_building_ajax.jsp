<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%@include file="/WEB-INF/jsp/ab/includes/flexpay_filter.jsp" %>

<script type="text/javascript">

    $(function() {
        FF.createFilter("country", {
            action: "<s:url action="countryFilterAjax" namespace="/dicts" includeParams="none"/>"
        });
        FF.createFilter("region", {
            action: "<s:url action="regionFilterAjax" namespace="/dicts" includeParams="none"/>",
            parents: ["country"]
        });
        FF.createFilter("town", {
            action: "<s:url action="townFilterAjax" namespace="/dicts" includeParams="none"/>",
            parents: ["region"]
        });
        FF.createFilter("street", {
            action: "<s:url action="streetFilterAjax" namespace="/dicts" includeParams="none"/>",
            parents: ["town"]
        });
        FF.createFilter("building", {
            action: "<s:url action="buildingFilterAjax" namespace="/dicts" includeParams="none"/>",
            isArray: true,
            parents: ["street"]
        });

        FF.filters["building"].addListener(function() {
            var filter = FF.filters["building"];
            $.post("<s:url action="apartmentsListAjax" namespace="/dicts" includeParams="none"/>",
                    {buildingId: filter.value.val()},
                    function(data) {
                        $("#result").html(data);
                    });
        });

        FF.filters["building"].addEraseFunction(function() {
            $("#result").html('<input type="submit" class="btn-exit" '
                    + 'onclick="$(\'#fobjects\').attr(\'action\',\'<s:url action="apartmentDelete" includeParams="none" />\');" '
                    + 'value="<s:text name="common.delete_selected"/>"/>\n'
                    + '<input type="submit" class="btn-exit" '
                    + 'onclick="$(\'#fobjects\').attr(\'action\',\'<s:url action="apartmentEdit"><s:param name="apartment.id" value="0"/></s:url>\');" '
                    + 'value="<s:text name="common.new"/>"/>');
        });

    });

    function pagerAjax(element) {
        var isSelect = element.name == "pager.pageSize";
        $.post("<s:url action="apartmentsListAjax" namespace="/dicts" includeParams="none"/>",
                {
                    buildingId: FF.filters["building"].value.val(),
                    "apartmentSorter.active": $("#apartmentSorterActive").val(),
                    "apartmentSorter.order": $("#apartmentSorterOrder").val(),
                    pageSizeChanged: isSelect,
                    "pager.pageNumber": isSelect ? "" : element.value,
                    "pager.pageSize": isSelect ? element.value : $('select[name="pager.pageSize"]').val()
                },
                function(data) {
                    $("#result").html(data);
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

<table width="100%">
    <tr>
        <td class="filter"><s:text name="ab.country"/></td>
        <td><%@include file="../ajax/country_search_filter.jsp" %></td>
        <td class="filter"><s:text name="ab.region"/></td>
        <td><%@include file="../ajax/region_search_filter.jsp" %></td>
        <td class="filter"><s:text name="ab.town"/></td>
        <td><%@include file="../ajax/town_search_filter.jsp" %></td>
    </tr>
	<tr>
		<td class="filter"><s:text name="ab.street"/></td>
		<td colspan="3"><%@include file="../ajax/street_search_filter.jsp" %></td>
		<td class="filter"><s:text name="ab.building"/></td>
		<td><%@include file="../ajax/building_search_filter.jsp" %></td>
	</tr>
</table>
