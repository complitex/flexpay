<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<%@include file="/WEB-INF/jsp/ab/includes/flexpay_filter.jsp"%>

<s:if test="#readonly == null">
    <s:set name="readonly" value="false" />
</s:if>

<script type="text/javascript">

    $(function() {
        FF.createFilter("country", {
            action: "<s:url action="countryFilterAjax" namespace="/dicts" includeParams="none" />",
            <s:if test="#readonly">display:"input-readonly",</s:if>
            defaultValue: "<s:text name="%{userPreferences.countryFilter}" />"
        });
        FF.createFilter("region", {
            action: "<s:url action="regionFilterAjax" namespace="/dicts" includeParams="none" />",
            parents: ["country"],
            <s:if test="#readonly">display:"input-readonly",</s:if>
            defaultValue: "<s:text name="%{userPreferences.regionFilter}" />"
        });
        FF.createFilter("town", {
            action: "<s:url action="townFilterAjax" namespace="/dicts" includeParams="none" />",
            parents: ["region"],
            <s:if test="#readonly">display:"input-readonly",</s:if>
            defaultValue: "<s:text name="%{userPreferences.townFilter}" />"
        });
        FF.createFilter("street", {
            action: "<s:url action="streetFilterAjax" namespace="/dicts" includeParams="none" />",
            parents: ["town"],
            <s:if test="#readonly">display:"input-readonly",</s:if>
            defaultValue: "<s:text name="%{userPreferences.streetFilter}" />"
        });
        FF.createFilter("building", {
            action: "<s:url action="buildingFilterAjax" namespace="/dicts" includeParams="none" />",
            isArray: true,
            parents: ["street"],
            <s:if test="#readonly">display:"input-readonly",</s:if>
            defaultValue: "<s:text name="%{userPreferences.buildingFilter}" />"
        });
        FF.createFilter("apartment", {
            action: "<s:url action="apartmentFilterAjax" namespace="/dicts" includeParams="none" />",
            isArray: true,
            parents: ["building"],
            <s:if test="#readonly">display:"input-readonly",</s:if>
            defaultValue: "<s:text name="%{userPreferences.apartmentFilter}" />"
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
		<td class="filter"><s:text name="ab.street" /></td>
		<td id="street_raw"></td>
		<td class="filter"><s:text name="ab.building" /></td>
		<td id="building_raw"></td>
        <td class="filter"><s:text name="ab.apartment" /></td>
        <td id="apartment_raw"></td>
	</tr>
</table>
