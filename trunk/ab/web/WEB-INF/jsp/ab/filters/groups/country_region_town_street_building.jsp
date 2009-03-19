
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
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
		<td><%@include file="../street_filter.jsp" %></td>
		<td class="filter"><s:text name="ab.building"/></td>
		<td><%@include file="../buildings_filter.jsp" %></td>
		<td colspan="2">&nbsp;</td>
	</tr>
</table>
