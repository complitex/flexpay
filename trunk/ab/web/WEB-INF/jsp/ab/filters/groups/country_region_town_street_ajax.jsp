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
    });

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
	</tr>
</table>
