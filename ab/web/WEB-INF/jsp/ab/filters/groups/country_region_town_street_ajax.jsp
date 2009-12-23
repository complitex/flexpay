<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
<%@include file="/WEB-INF/jsp/ab/includes/flexpay_filter.jsp"%>

<s:if test="#readonly == null">
    <s:set name="readonly" value="false" />
</s:if>

<script type="text/javascript">

    $(function() {
        FF.createFilter("country", {
            action: "<s:url action="countryFilterAjax" namespace="/dicts" includeParams="none" />",
            <s:if test="#readonly">display:"input-readonly",</s:if>
            defaultValue: "<s:property value="countryFilter != null ? countryFilter : userPreferences.countryFilter" />"
        });
        FF.createFilter("region", {
            action: "<s:url action="regionFilterAjax" namespace="/dicts" includeParams="none" />",
            parents: ["country"],
            <s:if test="#readonly">display:"input-readonly",</s:if>
            defaultValue: "<s:property value="regionFilter != null ? regionFilter : userPreferences.regionFilter" />"
        });
        FF.createFilter("town", {
            action: "<s:url action="townFilterAjax" namespace="/dicts" includeParams="none" />",
            parents: ["region"],
            <s:if test="#readonly">display:"input-readonly",</s:if>
            defaultValue: "<s:property value="townFilter != null ? townFilter : userPreferences.townFilter" />"
        });
        FF.createFilter("street", {
            action: "<s:url action="streetFilterAjax" namespace="/dicts" includeParams="none" />",
            parents: ["town"],
            <s:if test="#readonly">display:"input-readonly",</s:if>
            defaultValue: "<s:property value="streetFilter != null ? streetFilter : userPreferences.streetFilter" />"
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
