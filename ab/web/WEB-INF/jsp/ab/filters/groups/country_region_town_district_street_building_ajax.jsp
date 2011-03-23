<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
<%@include file="/WEB-INF/jsp/ab/includes/flexpay_filter.jsp"%>

<script type="text/javascript">

    $(function() {
        FF.createFilter("country", {
            action: "<s:url action="countryFilterAjax" namespace="/dicts" />",
            defaultValue: "<s:property value="countryFilter != null ? countryFilter : userPreferences.countryFilter" />"
        });
        FF.createFilter("region", {
            action: "<s:url action="regionFilterAjax" namespace="/dicts" />",
            parents: ["country"],
            defaultValue: "<s:property value="regionFilter != null ? regionFilter : userPreferences.regionFilter" />"
        });
        FF.createFilter("town", {
            action: "<s:url action="townFilterAjax" namespace="/dicts" />",
            parents: ["region"],
            defaultValue: "<s:property value="townFilter != null ? townFilter : userPreferences.townFilter" />"
        });
        FF.createFilter("district", {
            action: "<s:url action="districtFilterAjax" namespace="/dicts" />",
            parents: ["town"],
            defaultValue: "<s:property value="districtFilter != null ? districtFilter : userPreferences.districtFilter" />"
        });
        FF.createFilter("street", {
            action: "<s:url action="streetFilterAjax" namespace="/dicts" />",
            parents: ["town"],
            defaultValue: "<s:property value="streetFilter != null ? streetFilter : userPreferences.streetFilter" />"
        });
        FF.createFilter("building", {
            action: "<s:url action="buildingFilterAjax" namespace="/dicts" />",
            parents: ["district", "street"],
            isArray: true,
            defaultValue: "<s:property value="buildingFilter != null ? buildingFilter : userPreferences.buildingFilter" />"
        });
    });

</script>

<table width="100%">
    <tr>
        <td class="filter"><s:text name="ab.country" /></td>
        <td id="country_raw"></td>
        <td class="filter"><s:text name="ab.region" /></td>
        <td id="region_raw"></td>
        <td class="filter"><s:text name="ab.town" /></td>
        <td id="town_raw"></td>
    </tr>
	<tr>
        <td class="filter"><s:text name="ab.district" /></td>
        <td id="district_raw"></td>
        <td class="filter"><s:text name="ab.street" /></td>
        <td id="street_raw"></td>
		<td class="filter"><s:text name="ab.building" /></td>
        <td id="building_raw"></td>
	</tr>
</table>
