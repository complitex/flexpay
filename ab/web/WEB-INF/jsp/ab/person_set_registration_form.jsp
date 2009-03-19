<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%@include file="/WEB-INF/jsp/common/jquery_ui.jsp"%>

<script type="text/javascript">
    FP.calendars("#beginDate", "<s:url value="/resources/common/js/jquery/jquery-ui/images/calendar.gif" includeParams="none" />");
    FP.calendars("#endDate", "<s:url value="/resources/common/js/jquery/jquery-ui/images/calendar.gif" includeParams="none" />");
</script>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<!-- form action depends on  -->
	<form id="srform" method="post" action="<s:url includeParams="none" />">
		<s:hidden name="person.id" value="%{person.id}"/>
		<s:hidden name="editType" value="registration" />

		<tr class="cols_1">
			<td class="col_1s" colspan="4"><strong><s:text name="ab.person.registration_address"/></strong></td>
		</tr>
		<tr>
			<td class="filter"><s:text name="ab.country"/></td>
			<td>
				<%@include file="filters/country_filter.jsp" %>
			</td>
			<td class="filter"><s:text name="ab.region"/></td>
			<td>
				<%@include file="filters/region_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="filter"><s:text name="ab.town"/></td>
			<td>
				<%@include file="filters/town_filter.jsp" %>
			</td>
			<td class="filter"><s:text name="ab.street"/></td>
			<td>
				<%@include file="filters/street_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="filter"><s:text name="ab.building"/></td>
			<td>
				<%@include file="filters/buildings_filter.jsp" %>
			</td>
			<td class="filter"><s:text name="ab.apartment"/></td>
			<td>
				<%@include file="filters/apartment_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="filter">
				<s:text name="ab.person.registration.begin_date"/>
			</td>
			<td>
				<input type="text" name="beginDateStr" id="beginDate" value="<s:property value="format(beginDate)"/>" readonly="readonly" />
			</td>
			<td class="filter">
				<s:text name="ab.person.registration.end_date"/>
			</td>
			<td>
				<input type="text" name="endDateStr" id="endDate" value="<s:property value="format(endDate)"/>" readonly="readonly" />
			</td>
		</tr>


		<tr>
			<td colspan="3">
				<input type="submit" class="btn-exit" name="submitted" id="sbmt-btn" value="<s:text name="common.save"/>"/>
			</td>
		</tr>
	</form>
</table>
