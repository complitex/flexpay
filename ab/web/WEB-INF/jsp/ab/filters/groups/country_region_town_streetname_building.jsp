<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%@include file="/WEB-INF/jsp/common/includes/flexpay_filter.jsp" %>

<%--
<script type="text/javascript">

    $(function() {
        FF.createFilter("country", {action: "<s:url action="countryFilterAjax" namespace="/dicts" includeParams="none"/>"});
        FF.createFilter("region", {action: "<s:url action="regionFilterAjax" namespace="/dicts" includeParams="none"/>", parents: ["country"]});
        FF.createFilter("town", {action: "<s:url action="townFilterAjax" namespace="/dicts" includeParams="none"/>", parents: ["region"]});
        FF.createFilter("street", {action: "<s:url action="streetFilterAjax" namespace="/dicts" includeParams="none"/>", parents: ["town"]});
        FF.createFilter("building", {
            action: "<s:url action="buildingFilterAjax" namespace="/dicts" includeParams="none"/>",
            isArray: true,
            parents: ["street"],
            resultAction: "<s:url action="apartmentsListAjax" namespace="/dicts" includeParams="none"/>"
        });
    });

</script>
--%>

<table width="100%">
    <tr>
        <td class="filter"><s:text name="ab.country"/></td>
        <td><%@include file="../country_filter.jsp" %></td>
        <td class="filter"><s:text name="ab.region"/></td>
        <td><%@include file="../region_filter.jsp" %></td>
        <td class="filter"><s:text name="ab.town"/></td>
        <td><%@include file="../town_filter.jsp" %></td>
    </tr>
	<tr>
		<td class="filter"><s:text name="ab.street"/></td>
		<td colspan="3"><%@include file="../street_search_filter.jsp" %></td>
		<td class="filter"><s:text name="ab.building"/></td>
		<td><%@include file="../buildings_filter.jsp" %></td>
	</tr>
</table>
