<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form id="fregions" method="post" action="">

		<tr>
			<td class="th" width="100%" colspan="4" align="center">
				<%@ include file="filters/country_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th" width="100%" colspan="4" align="center">
				<%@ include file="filters/date_interval_filter.jsp" %>
			</td>
		</tr>

		<tr>
			<td class="th" width="1%">&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox"
								  onchange="FP.setCheckboxes(this.checked, 'regionIds')">
			</td>
			<td class="th" width="63%"><s:text name="ab.region_name"/></td>
			<td class="th" width="35%">&nbsp;</td>
		</tr>
		<c:forEach items="${requestScope['region_names']}" varStatus="status"
				   var="regionName">
			<tr valign="middle" class="cols_1">
				<td class="col_1s"><c:out value="${status.index + 1}"/></td>
				<td class="col"><input type="checkbox"
						   value="<c:out value="${regionName.region.id}"/>"
						   name="<c:out value="regionIds"/>"></td>
				<td class="col"><c:out value="${regionName.translation.name}"/></td>
				<td class="col">
					<a href="<c:url value="/dicts/edit_region.action?id=${regionName.region.id}"/>"><s:text
							name="common.edit_selected"/></a></td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="4" height="3" bgcolor="#4a4f4f"/>
		<tr>
			<td colspan="4">
				<input type="submit" class="btn-exit"
					   onclick="$('fregions').action='<c:url value="/dicts/delete_regions.action"/>';$('fregions').submit()"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<c:url value="/dicts/create_region.action"/>'"
					   value="<s:text name="common.new"/>"/>
				<input type="submit" class="btn-exit"
					   value="<s:text name="common.refresh"/>"/>
			</td>
		</tr>
	</form>
</table>
