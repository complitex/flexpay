<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%@include file="/WEB-INF/jsp/ab/includes/flexpay_filter.jsp" %>

<script type="text/javascript">

    $(function() {
        FF.createFilter("country", {
            action: "<s:url action="countryFilterAjax" namespace="/dicts" includeParams="none"/>",
            defaultValue: "<s:text name="%{userPreferences.countryFilterValue}" />"
        });
        FF.createFilter("region", {
            action: "<s:url action="regionFilterAjax" namespace="/dicts" includeParams="none"/>",
            parents: ["country"],
            defaultValue: "<s:text name="%{userPreferences.regionFilterValue}" />"
        });
        FF.createFilter("town", {
            action: "<s:url action="townFilterAjax" namespace="/dicts" includeParams="none"/>",
            parents: ["region"],
            defaultValue: "<s:text name="%{userPreferences.townFilterValue}" />"
        });
        FF.createFilter("street", {
            action: "<s:url action="streetFilterAjax" namespace="/dicts" includeParams="none"/>",
            parents: ["town"],
            defaultValue: "<s:text name="%{userPreferences.streetFilterValue}" />"
        });
        FF.createFilter("building", {
            action: "<s:url action="buildingFilterAjax" namespace="/dicts" includeParams="none"/>",
            isArray: true,
            parents: ["street"],
            defaultValue: "<s:text name="%{userPreferences.buildingFilterValue}" />"
        });

        FF.addListener("building", function(filter) {
            var id = "result";
            FF.loading(id);
            $.post("<s:url action="apartmentsListAjax" namespace="/dicts" includeParams="none"/>",
                    {buildingId: filter.value.val()},
                    function(data) {
                        $("#" + id).html(data);
                    });
        });

        FF.addEraseFunction("building", function(filter) {
            $("#result").html('<input type="submit" class="btn-exit" '
                    + 'onclick="$(\'#fobjects\').attr(\'action\',\'<s:url action="apartmentDelete" includeParams="none" />\');" '
                    + 'value="<s:text name="common.delete_selected"/>"/>\n'
                    + '<input type="button" class="btn-exit" '
                    + 'onclick="window.location = \'<s:url action="apartmentEdit"><s:param name="apartment.id" value="0"/></s:url>\'" '
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
        <td id="country_raw"></td>
        <td class="filter"><s:text name="ab.region"/></td>
        <td id="region_raw"></td>
        <td class="filter"><s:text name="ab.town"/></td>
        <td id="town_raw"></td>
    </tr>
	<tr>
		<td class="filter"><s:text name="ab.street"/></td>
		<td id="street_raw" colspan="3"></td>
		<td class="filter"><s:text name="ab.building"/></td>
		<td id="building_raw"></td>
	</tr>
</table>
