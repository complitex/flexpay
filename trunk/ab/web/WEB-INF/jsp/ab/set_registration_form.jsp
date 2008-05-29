<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<% 
	String personId = request.getParameter("person.id");
	if(personId != null && !personId.equals("")) {
		session.putValue(org.flexpay.ab.actions.person.SetRegistrationAction.PERSON_SESSION_KEY, personId);
	}
%>


<s:actionerror/>

<%
	if("true".equals(request.getParameter("apartment_not_selected"))) {
		%>
		<font color="red"><s:text name="ab.person.registration_address.apartment_nor_selected"/></font>
		<%
	}
%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<s:form id="srform" action="setRegistrationForm">
	<form id="srform" method="post" action="<s:url value="/dicts/setRegistrationForm" />">
		<input type="hidden" name="personId" value="<%=request.getParameter("person.id") %>" />
	<tr>
		<td class="filter"><s:text name="ab.country"/></td>
		<td><%@include file="filters/country_filter.jsp" %></td>
		<td class="filter"><s:text name="ab.region"/></td>
		<td><%@include file="filters/region_filter.jsp" %></td>
	</tr>
	<tr>
		<td class="filter"><s:text name="ab.town"/></td>
		<td><%@include file="filters/town_filter.jsp" %></td>
		<td class="filter"><s:text name="ab.street"/></td>
		<td><%@include file="filters/street_filter.jsp" %></td>
	</tr>
	<tr>
		<td class="filter"><s:text name="ab.building"/></td>
		<td><%@include file="filters/buildings_filter.jsp" %></td>
		<td class="filter"><s:text name="ab.apartment"/></td>
		<td>
		<select name="apartmentId" class="form-select">
			<s:iterator value="apartments">
				<option  value="<s:property value="%{id}"/>">
					<s:property value="number"/>
				</option>
			</s:iterator>
		</select>
		</td>
	</tr>
	<tr>
		<td class="filter">
			Start date
		</td>
		<td>
			<s:textfield name="beginDate" id="beginDate" value="%{beginDate}"/>
			
		</td>
		<td class="filter">
			End date
		</td>
		<td>
			<s:textfield name="endDate" id="endDate" value="%{endDate}"/>
			
		</td>
	</tr>

		
		
		<tr>
			<td colspan="3">
				<input type="submit" class="btn-exit"
					   onclick="$('srform').action='<s:url action="setRegistration"/>';$('srform').submit()"
					   value="<s:text name="common.save"/>"/>
			</td>
		</tr>
	</s:form>
</table>
