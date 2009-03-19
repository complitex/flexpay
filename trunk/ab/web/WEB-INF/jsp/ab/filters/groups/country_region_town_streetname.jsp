<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
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
		<td colspan="5"><%@include file="../street_search_filter.jsp" %></td>
	</tr>
<%@include file="footer.jsp" %>
