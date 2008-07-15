<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<!-- form action depends on  -->
	<form id="srform" method="post" action="<s:url includeParams="none" />">
		<s:hidden name="person.id" value="%{person.id}"/>
		<s:hidden name="editType" value="registration" />

		<tr class="cols_1">
			<td class="col_1s" colspan="4"><b><s:text name="ab.person.registration_address"/></b></td>
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
				<input type="text" name="beginDateStr" id="beginDate"
						value="<s:property value="format(beginDate)"/>" />
				<img src="<s:url value="/resources/common/js/jscalendar/img.gif" includeParams="none"/>" alt=""
					 id="trigger.beginDate"
					 style="cursor: pointer; border: 1px solid red;"
					 title="<s:text name="common.calendar"/>"
					 onmouseover="this.style.background='red';"
					 onmouseout="this.style.background=''"/>
				<script type="text/javascript">
				Calendar.setup({
					inputField	 : "beginDate",
					ifFormat	 : "%Y/%m/%d",
					button		 : "trigger.beginDate",
					align		 : "Tl"
				});
				</script>
			</td>
			<td class="filter">
				<s:text name="ab.person.registration.end_date"/>
			</td>
			<td>
				<input type="text" name="endDateStr" id="endDate"
						value="<s:property value="format(endDate)"/>" />
				<img src="<s:url value="/resources/common/js/jscalendar/img.gif" includeParams="none"/>" alt=""
					 id="trigger.endDate"
					 style="cursor: pointer; border: 1px solid red;"
					 title="<s:text name="common.calendar"/>"
					 onmouseover="this.style.background='red';"
					 onmouseout="this.style.background=''"/>
				<script type="text/javascript">
				Calendar.setup({
					inputField	 : "endDate",
					ifFormat	 : "%Y/%m/%d",
					button		 : "trigger.endDate",
					align		 : "Tl"
				});
				</script>

			</td>
		</tr>


		<tr>
			<td colspan="3">
				<input type="submit" class="btn-exit" name="submitted" id="sbmt-btn" value="<s:text name="common.save"/>"/>
			</td>
		</tr>
	</form>
</table>
