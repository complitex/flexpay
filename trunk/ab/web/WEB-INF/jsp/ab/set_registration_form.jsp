<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form id="srform" method="post" action="<s:url value="/dicts/setRegistrationAction.action" includeParams="none" />">
		
		<input type="hidden" name="person.id" value="<s:property value="person.id"/>" />
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
		<select name="apartment.id" class="form-select">
			<s:iterator value="apartments">
				<option  value="<s:property value="%{id}"/>" <s:if test="%{id == person.apartment.id}"> selected</s:if> >
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
					   onclick="$('srform').action='<s:url value="/dicts/setRegistrationAction.action" includeParams="none" />?action=save';$('srform').submit()"
					   value="<s:text name="common.save"/>"/>
			</td>
		</tr>
	</form>
</table>


<s:text name="%{apartmentError}" />
<s:text name="%{beginAfterEndError}" />
<s:if test="beginIntervalError != null">
	<s:text name="%{beginIntervalError}" /> <s:property value="person.beginValidInterval[0]"/> - <s:property value="person.beginValidInterval[1]"/> 
</s:if>

