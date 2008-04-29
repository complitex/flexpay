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
			<td class="th" width="100%" colspan="4" align="center">
				<%@ include file="filters/country_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th" width="100%" colspan="4" align="center">
				<%@ include file="filters/region_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th" width="100%" colspan="4" align="center">
				<%@ include file="filters/town_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th" width="100%" colspan="4" align="center">
				<%@ include file="filters/street_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th" width="100%" colspan="4" align="center">
				<%@ include file="filters/buildings_filter.jsp" %>
			</td>
		</tr>

		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="98%">
				<s:text name="ab.apartment"/> 
			</td>
		</tr>
		<s:iterator value="%{apartments}" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right">
					<s:property	value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
					&nbsp;
				</td>
				<td class="col">
					<input type="radio" value="<s:property value="%{id}"/>" name="apartmentId"/>
					<s:property	value="%{number}"/>
				</td>
			</tr>
		</s:iterator>
		<tr class="cols_1">
			<td class="col" width="100%" colspan="3" align="center">
				<%@ include file="filters/pager.jsp" %>
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
