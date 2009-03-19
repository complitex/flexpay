
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<form id="srform" method="post" action="<s:url action="eircAccountCreateForm1" includeParams="none" />">
    <table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="filter"><s:text name="ab.country"/></td>
			<td>
				<%@include file="/WEB-INF/jsp/ab/filters/country_filter.jsp" %>
			</td>
			<td class="filter"><s:text name="ab.region"/></td>
			<td>
				<%@include file="/WEB-INF/jsp/ab/filters/region_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="filter"><s:text name="ab.town"/></td>
			<td>
				<%@include file="/WEB-INF/jsp/ab/filters/town_filter.jsp" %>
			</td>
			<td class="filter"><s:text name="ab.street"/></td>
			<td>
				<%@include file="/WEB-INF/jsp/ab/filters/street_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="filter"><s:text name="ab.building"/></td>
			<td>
				<%@include file="/WEB-INF/jsp/ab/filters/buildings_filter.jsp" %>
			</td>
			<td class="filter"><s:text name="ab.apartment"/></td>
			<td>
				<select name="apartmentId" class="form-select">
					<s:iterator value="apartments">
						<option value="<s:property value="%{id}"/>">
							<s:property value="number"/>
						</option>
					</s:iterator>
				</select>
			</td>
		</tr>


		<tr>
			<td colspan="3">
				<input type="submit" class="btn-exit"
					   onclick="$('#srform').attr('action', '<s:url action="eircAccountCreateForm2" includeParams="none" />');"
					   value="<s:text name="common.next"/>"/>
			</td>
		</tr>
    </table>
</form>
