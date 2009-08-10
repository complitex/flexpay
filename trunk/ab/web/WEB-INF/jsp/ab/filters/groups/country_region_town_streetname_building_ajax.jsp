<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%@include file="/WEB-INF/jsp/ab/includes/flexpay_filter.jsp" %>

<script type="text/javascript">

    $(function() {
        FF.createFilter("country", {
            action: "<s:url action="countryFilterAjax" namespace="/dicts" includeParams="none"/>",
            defaultValue: "<s:text name="%{userPreferences.countryFilter}" />"
        });
        FF.createFilter("region", {
            action: "<s:url action="regionFilterAjax" namespace="/dicts" includeParams="none"/>",
            parents: ["country"],
            defaultValue: "<s:text name="%{userPreferences.regionFilter}" />"
        });
        FF.createFilter("town", {
            action: "<s:url action="townFilterAjax" namespace="/dicts" includeParams="none"/>",
            parents: ["region"],
            defaultValue: "<s:text name="%{userPreferences.townFilter}" />"
        });
        FF.createFilter("street", {
            action: "<s:url action="streetFilterAjax" namespace="/dicts" includeParams="none"/>",
            parents: ["town"],
            defaultValue: "<s:text name="%{userPreferences.streetFilter}" />"
        });
        FF.createFilter("building", {
            action: "<s:url action="buildingFilterAjax" namespace="/dicts" includeParams="none"/>",
            isArray: true,
            parents: ["street"],
            defaultValue: "<s:text name="%{userPreferences.buildingFilter}" />"
        });

    });

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
