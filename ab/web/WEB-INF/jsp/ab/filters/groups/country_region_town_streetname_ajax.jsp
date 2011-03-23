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
        FF.createFilter("street", {
            action: "<s:url action="streetFilterAjax" namespace="/dicts" />",
            parents: ["town"],
            defaultValue: "",
            defaultString: "<s:text name="ab.street.search" />",
            preRequest: false,
            required: false
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
        <td id="street_raw" colspan="5"></td>
	</tr>
</table>
