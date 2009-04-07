
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="header.jsp" %>
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
		<td colspan="5"><%@include file="../ajax/street_search_filter.jsp" %></td>
	</tr>
	<tr>
		<td class="filter"><s:text name="ab.district"/></td>
		<td colspan="3"><%@include file="../district_filter.jsp" %></td>
		<td class="filter"><s:text name="ab.building"/></td>
		<td><%@include file="../buildings_filter.jsp" %></td>
	</tr>
<%@include file="footer.jsp" %>
