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
</table>
